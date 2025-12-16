package ma.aui.sse.it.xcommerce.monolithic.utils.jackson;

import com.fasterxml.jackson.databind.Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public Module jpaEntityBlockerModule() {
        return new JpaEntityBlockerModule();
    }
}