package ma.aui.sse.it.xcommerce.monolithic.utils.jackson;

import com.fasterxml.jackson.databind.Module;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class JacksonConfig {

    @Produces
    @ApplicationScoped
    public Module jpaEntityBlockerModule() {
        return new JpaEntityBlockerModule();
    }
}