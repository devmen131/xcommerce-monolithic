package ma.aui.sse.it.xcommerce.monolithic.utils.jackson;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class JpaEntityBlockerModule extends SimpleModule {

    public JpaEntityBlockerModule() {
        super("JpaEntityBlockerModule");
        this.setSerializerModifier(new JpaEntitySerializerModifier());
    }
}