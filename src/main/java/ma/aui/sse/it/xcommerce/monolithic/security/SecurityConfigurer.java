package ma.aui.sse.it.xcommerce.monolithic.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfigurer {

    @Autowired
    private DataSource dataSource;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/rest/user/admin")
                    .hasRole("SUPERADMIN")
                    .requestMatchers("/rest/user/authenticate")
                    .permitAll()
                    .requestMatchers(HttpMethod.GET, "/rest/catalog/**")
                    .permitAll()
                    .requestMatchers("/rest/catalog/**")
                    .hasRole("ADMIN")
                    .anyRequest()
                    //Permit All pour toutes les urls restantes (sb 3.0 par default toutes les url sont sécurisé)
                    .permitAll()
            )
            .addFilterBefore(new JwtInterceptingFilter(), UsernamePasswordAuthenticationFilter.class)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                   .jdbcAuthentication()
                   .dataSource(dataSource)
                   .usersByUsernameQuery("select username, password, active as enabled from \"user\" where username = ?")
                   .authoritiesByUsernameQuery("select username, authority from authority where username = ?")
                   .passwordEncoder(passwordEncoder)
                   .and()
                   .build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}