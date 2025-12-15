package ma.aui.sse.it.xcommerce.monolithic.utils.jackson;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.io.IOException;

public class ForbiddenEntitySerializer extends JsonSerializer<Object> {

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {

        throw JsonMappingException.from(
                gen,
                "Serializing JPA entities not Allowed : " + value.getClass().getName()
        );
    }
}