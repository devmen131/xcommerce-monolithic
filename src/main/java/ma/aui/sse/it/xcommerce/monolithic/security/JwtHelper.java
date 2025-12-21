package ma.aui.sse.it.xcommerce.monolithic.security;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class JwtHelper {

    private JwtHelper() {
    }

    public static String generateToken(String username, Set<String> roles) {
        Instant now = Instant.now();
        Instant expiresAt = now.plus(Duration.ofDays(1));
        Set<String> safeRoles = roles != null ? roles : Set.of();
        return Jwt.issuer("xcommerce")
                .upn(username)
                .groups(new HashSet<>(safeRoles))
                .issuedAt(now.getEpochSecond())
                .expiresAt(expiresAt.getEpochSecond())
                .sign();
    }
}
