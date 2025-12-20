package ma.aui.sse.it.xcommerce.monolithic.utils.jackson;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import ma.aui.sse.it.xcommerce.monolithic.data.dtos.ProductDto;

import java.io.IOException;

public class ProductDtoKeyDeserializer extends KeyDeserializer {
    @Override
    public Object deserializeKey(String s, DeserializationContext deserializationContext) throws IOException {
        return new ProductDto(Long.parseLong(s));
    }
}
