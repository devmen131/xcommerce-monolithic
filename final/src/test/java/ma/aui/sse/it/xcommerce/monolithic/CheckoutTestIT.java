package ma.aui.sse.it.xcommerce.monolithic;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import ma.aui.sse.it.xcommerce.monolithic.data.dtos.OrderDto;
import ma.aui.sse.it.xcommerce.monolithic.data.dtos.ProductCartDto;
import ma.aui.sse.it.xcommerce.monolithic.data.dtos.ShoppingCartDto;
import ma.aui.sse.it.xcommerce.monolithic.data.entities.OrderStatus;
import ma.aui.sse.it.xcommerce.monolithic.data.entities.Product;
import ma.aui.sse.it.xcommerce.monolithic.data.repositories.ProductRepository;
import ma.aui.sse.it.xcommerce.monolithic.services.ShoppingCartService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CheckoutTestIT extends AbstractTestIT {

    private static final long PRODUCT_ID_S10 = 1;
    private static final long PRODUCT_ID_IPHONE_X = 2;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ObjectWriter writer = objectMapper.writer()
                                                    .withDefaultPrettyPrinter();

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Test
    public void givenTwoProducts_whenCheckout_thenOrderCreatedAndCartCleared() throws Exception {
        //Given
        final ProductCartDto productCartDtoIphoneX = new ProductCartDto();
        productCartDtoIphoneX.setProductId(PRODUCT_ID_IPHONE_X);
        productCartDtoIphoneX.setQuantity(1);

        final ProductCartDto productCartDtoS10 = new ProductCartDto();
        productCartDtoS10.setProductId(PRODUCT_ID_S10);
        productCartDtoS10.setQuantity(1);

        mockMvc.perform(patch("/rest/shoppingCart/addProduct")
                       .content(writer.writeValueAsString(productCartDtoIphoneX))
                       .accept(MediaType.APPLICATION_JSON)
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk());
        mockMvc.perform(patch("/rest/shoppingCart/addProduct")
                       .content(writer.writeValueAsString(productCartDtoS10))
                       .accept(MediaType.APPLICATION_JSON)
                       .contentType(MediaType.APPLICATION_JSON))
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
                                                  .get();
        Product productS10 = productRepository.findById(PRODUCT_ID_S10)
                                              .get();
        float exceptedPrice = productIphoneX.getPrice() + productS10.getPrice();
        assertEquals(exceptedPrice, order.getProductsTotalPrice(), 0.01);
        assertEquals(400f, order.getShippingCost(), 0.01);

        ShoppingCartDto shoppingCartDto = shoppingCartService.getShoppingCart(1);
        assertNotNull(shoppingCartDto);
        assertTrue(shoppingCartDto.isEmpty());
    }
}
