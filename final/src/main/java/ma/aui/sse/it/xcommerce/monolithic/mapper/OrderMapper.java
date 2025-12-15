package ma.aui.sse.it.xcommerce.monolithic.mapper;

import ma.aui.sse.it.xcommerce.monolithic.data.dtos.OrderDto;
import ma.aui.sse.it.xcommerce.monolithic.data.dtos.OrderLineDto;
import ma.aui.sse.it.xcommerce.monolithic.data.entities.Order;
import ma.aui.sse.it.xcommerce.monolithic.data.entities.OrderLine;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "userName", source = "user.username")
    @Mapping(target = "userId", source = "user.id")
    OrderDto toOrderDto(Order order);
    List<OrderDto> toOrderDto(List<Order> orderList);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "orderId", source = "order.id")
    OrderLineDto toOrderLineDto(OrderLine orderLine);
}
