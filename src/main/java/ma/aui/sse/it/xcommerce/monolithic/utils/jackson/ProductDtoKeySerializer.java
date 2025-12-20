package ma.aui.sse.it.xcommerce.monolithic.utils.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import ma.aui.sse.it.xcommerce.monolithic.data.dtos.ProductDto;

import java.io.IOException;

public class ProductDtoKeySerializer extends JsonSerializer<ProductDto> {

    @Override
    public void serialize(ProductDto productDto, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (productDto != null) {
            jsonGenerator.writeFieldName(String.valueOf(productDto.getId()));
        }
    }
}
