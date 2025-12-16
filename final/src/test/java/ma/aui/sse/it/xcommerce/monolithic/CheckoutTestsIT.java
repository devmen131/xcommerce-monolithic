package ma.aui.sse.it.xcommerce.monolithic;

import com.fasterxml.jackson.core.type.TypeReference;
import ma.aui.sse.it.xcommerce.monolithic.data.dtos.OrderDto;
import ma.aui.sse.it.xcommerce.monolithic.data.dtos.ProductCartDto;
import ma.aui.sse.it.xcommerce.monolithic.data.dtos.ShoppingCartDto;
import ma.aui.sse.it.xcommerce.monolithic.data.entities.OrderStatus;
import ma.aui.sse.it.xcommerce.monolithic.data.entities.Product;
import ma.aui.sse.it.xcommerce.monolithic.data.repositories.ProductRepository;
import ma.aui.sse.it.xcommerce.monolithic.services.OrderService;
import ma.aui.sse.it.xcommerce.monolithic.services.ShoppingCartService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CheckoutTestsIT extends AbstractTestIT {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private OrderService orderService;

    @Test
    public void givenTwoProducts_whenCheckout_thenOrderCreatedAndCartCleared() throws Exception {
        //Given
        final ProductCartDto productCartDtoIphoneX = getProductCartDtoIphoneX(1);
        final ProductCartDto productCartDtoS10 = getProductCartDtoS10(1);

        addProduct(productCartDtoIphoneX)
                .andExpect(status().isOk());
        addProduct(productCartDtoS10)
                .andExpect(status().isOk());

        //When
        mockMvc.perform(get("/rest/order/checkout"))
               .andExpect(status().isOk());

        //Then
        String response = mockMvc.perform(get("/rest/order/list"))
                                 .andExpect(status().isOk())
                                 .andReturn()
                                 .getResponse()
                                 .getContentAsString();

        List<OrderDto> orderList = objectMapper.readValue(response, new TypeReference<List<OrderDto>>() {});

        assertNotNull(orderList);
        assertEquals(1, orderList.size());
        OrderDto order = orderList.get(0);
        assertNotNull(order.getOrderLines());
        assertEquals(2, order.getOrderLines()
                             .size());
        assertEquals(OrderStatus.HANDLING.name(), order.getStatus());
        Product productIphoneX = productRepository.findById(PRODUCT_ID_IPHONE_X)
                                                  .orElse(null);
        Product productS10 = productRepository.findById(PRODUCT_ID_S10)
                                              .orElse(null);
        assertNotNull(productIphoneX);
        assertNotNull(productS10);
        float exceptedPrice = productIphoneX.getPrice() + productS10.getPrice();
        assertEquals(exceptedPrice, order.getProductsTotalPrice(), 0.01);
        assertEquals(400f, order.getShippingCost(), 0.01);

        ShoppingCartDto shoppingCartDto = shoppingCartService.getShoppingCart(1);
        assertNotNull(shoppingCartDto);
        assertTrue(shoppingCartDto.isEmpty());
    }

    @Test
    public void givenEmptyCart_whenCheckout_thenOrderListIsEmpty() throws Exception {
        //Given Empty Cart
        //When
        mockMvc.perform(get("/rest/order/checkout"))
               .andExpect(status().isOk());

        //Then
        List<OrderDto> orderDtoList = orderService.getOrdersByCustomer(1);
        assertNotNull(orderDtoList);
        assertTrue(orderDtoList.isEmpty());
    }

}
