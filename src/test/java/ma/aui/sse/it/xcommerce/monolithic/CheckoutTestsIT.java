package ma.aui.sse.it.xcommerce.monolithic;

import ma.aui.sse.it.xcommerce.monolithic.data.dtos.OrderDto;
import ma.aui.sse.it.xcommerce.monolithic.data.dtos.ProductCartDto;
import ma.aui.sse.it.xcommerce.monolithic.data.dtos.ProductDto;
import ma.aui.sse.it.xcommerce.monolithic.data.dtos.ShoppingCartDto;
import ma.aui.sse.it.xcommerce.monolithic.data.entities.OrderStatus;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static io.restassured.RestAssured.given;

@QuarkusTest
public class CheckoutTestsIT extends AbstractTestIT {

    @Test
    public void givenTwoProducts_whenCheckout_thenOrderCreatedAndCartCleared() throws Exception {
        //Given
        final ProductCartDto productCartDtoIphoneX = getProductCartDtoIphoneX(1);
        final ProductCartDto productCartDtoS10 = getProductCartDtoS10(1);

        addProduct(productCartDtoIphoneX)
                .statusCode(200);
        addProduct(productCartDtoS10)
                .statusCode(200);

        //When
        given()
                .when()
                .get("/rest/order/checkout")
                .then()
                .statusCode(200);

        //Then
        List<OrderDto> orderList = getOrdersByCustomer(USER_ID);

        assertNotNull(orderList);
        assertEquals(1, orderList.size());
        OrderDto order = orderList.get(0);
        assertNotNull(order.getOrderLines());
        assertEquals(2, order.getOrderLines()
                             .size());
        assertEquals(OrderStatus.HANDLING.name(), order.getStatus());

        ProductDto productIphoneX = getProductDto(PRODUCT_ID_IPHONE_X);
        ProductDto productS10 = getProductDto(PRODUCT_ID_S10);

        assertNotNull(productIphoneX);
        assertNotNull(productS10);
        float exceptedPrice = productIphoneX.getPrice() + productS10.getPrice();
        assertEquals(exceptedPrice, order.getProductsTotalPrice(), 0.01);
        assertEquals(400f, order.getShippingCost(), 0.01);

        ShoppingCartDto shoppingCartDto = getShoppingCartDto();
        assertNotNull(shoppingCartDto);
        assertTrue(shoppingCartDto.isEmpty());
    }

    @Test
    public void givenEmptyCart_whenCheckout_thenOrderListIsEmpty() throws Exception {
        //Given Empty Cart
        //When
        given()
                .when()
                .get("/rest/order/checkout")
                .then()
                .statusCode(200);

        //Then
        List<OrderDto> orderDtoList = getOrdersByCustomer(USER_ID);
        assertNotNull(orderDtoList);
        assertTrue(orderDtoList.isEmpty());
    }

}
