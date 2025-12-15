package ma.aui.sse.it.xcommerce.monolithic.mapper;

import ma.aui.sse.it.xcommerce.monolithic.data.dtos.ProductDto;
import ma.aui.sse.it.xcommerce.monolithic.data.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    ProductDto toProductDto(Product product);

}
