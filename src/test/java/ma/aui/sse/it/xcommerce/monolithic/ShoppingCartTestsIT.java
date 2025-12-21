package ma.aui.sse.it.xcommerce.monolithic;

import ma.aui.sse.it.xcommerce.monolithic.data.dtos.ProductCartDto;
import ma.aui.sse.it.xcommerce.monolithic.data.dtos.ProductDto;
import ma.aui.sse.it.xcommerce.monolithic.data.dtos.ShoppingCartDto;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

@QuarkusTest
public class ShoppingCartTestsIT extends AbstractTestIT {

    @Test
    public void givenOneProduct_WhenAddToCart_thenNumberOfProductIsOne() throws Exception {
        //Given
        ProductCartDto productCartDtoIphoneX = getProductCartDtoIphoneX(2);

        //When
        addProduct(productCartDtoIphoneX)
                .statusCode(200);

        //Then
        ShoppingCartDto shoppingCartDto = getShoppingCartDto();
        assertNotNull(shoppingCartDto);
        assertEquals(1, shoppingCartDto.getSelectedProducts()
                                       .size());

    }

    @Test
    public void givenTwoProduct_WhenAddToCart_thenNumberOfProductIsTwo() throws Exception {
        //Given
        ProductCartDto productCartDtoIphoneX = getProductCartDtoIphoneX(2);
        ProductCartDto productCartDtoS10 = getProductCartDtoS10(1);

        //When
        addProduct(productCartDtoIphoneX)
                .statusCode(200);
        addProduct(productCartDtoS10)
                .statusCode(200);

        //Then
        ShoppingCartDto shoppingCartDto = getShoppingCartDto();
        assertNotNull(shoppingCartDto);
        assertEquals(2, shoppingCartDto.getSelectedProducts()
                                       .size());
        ProductDto productDtoIphoneX = findProductInCart(shoppingCartDto, productCartDtoIphoneX.getId());
        assertNotNull(productDtoIphoneX);
        assertEquals(2, shoppingCartDto.getSelectedProducts()
                                       .getOrDefault(productDtoIphoneX, 0)
                                       .intValue());
    }

    private ProductDto findProductInCart(ShoppingCartDto shoppingCartDto, long productId) {
        return shoppingCartDto.getSelectedProducts()
                              .keySet()
                              .stream()
                              .filter(p -> p.getId() == productId)
                              .findFirst()
                              .orElse(null);
    }

    @Test
    public void givenTwoProduct_WhenRemoveOneFromCart_thenNumberOfProductIsOne() throws Exception {
        //Given
        ProductCartDto productCartDtoIphoneX = getProductCartDtoIphoneX(2);
        ProductCartDto productCartDtoS10 = getProductCartDtoS10(1);

        addProduct(productCartDtoIphoneX)
                .statusCode(200);
        addProduct(productCartDtoS10)
                .statusCode(200);

        //When
        given()
                .contentType(JSON)
                .accept(JSON)
                .body(convertToJson(productCartDtoS10))
                .when()
                .patch("/rest/shoppingCart/removeProduct")
                .then()
                .statusCode(200);

        //Then
        ShoppingCartDto shoppingCartDto = getShoppingCartDto();
        assertNotNull(shoppingCartDto);
        assertNotNull(shoppingCartDto.getSelectedProducts());
        assertEquals(1, shoppingCartDto.getSelectedProducts()
                                       .size());

        ProductDto productDto = findProductInCart(shoppingCartDto, productCartDtoS10.getId());
        assertNull(productDto);

    }

    @Test
    public void givenSameProduct_WhenMultiAddToCart_thenNumberOfProductIsOne() throws Exception {
        //Given
        ProductCartDto productCartDtoS10 = getProductCartDtoS10(1);

        //When
        addProduct(productCartDtoS10)
                .statusCode(200);
        productCartDtoS10.setQuantity(2);
        addProduct(productCartDtoS10)
                .statusCode(200);

        //Then
        ShoppingCartDto shoppingCartDto = getShoppingCartDto();
        assertNotNull(shoppingCartDto);
        assertEquals(1, shoppingCartDto.getSelectedProducts()
                                       .size());
        ProductDto productDto = findProductInCart(shoppingCartDto, productCartDtoS10.getId());
        assertNotNull(productDto);
        int quantity = shoppingCartDto.getSelectedProducts()
                                      .get(productDto);
        assertEquals(3, quantity);

    }

    @Test
    public void givenProductWithTwoQuantity_WhenDecrease_thenQuantityIsOne() throws Exception {

        ProductCartDto productCartDtoS10 = getProductCartDtoS10(2);
        addProduct(productCartDtoS10).statusCode(200);

        productCartDtoS10.setQuantity(1);
        given()
                .contentType(JSON)
                .accept(JSON)
                .body(convertToJson(productCartDtoS10))
                .when()
                .patch("/rest/shoppingCart/decreaseProductQuantity")
                .then()
                .statusCode(200);

        ShoppingCartDto shoppingCartDto = getShoppingCartDto();
        assertNotNull(shoppingCartDto);
        ProductDto productDto = findProductInCart(shoppingCartDto, productCartDtoS10.getId());
        assertEquals(1, shoppingCartDto.getSelectedProducts()
                                       .get(productDto)
                                       .intValue());
    }

    @Test
    public void givenProductWithOneQuantity_WhenDecrease_thenCartIsEmpty() throws Exception {

        ProductCartDto productCartDtoS10 = getProductCartDtoS10(1);
        addProduct(productCartDtoS10).statusCode(200);

        given()
                .contentType(JSON)
                .accept(JSON)
                .body(convertToJson(productCartDtoS10))
                .when()
                .patch("/rest/shoppingCart/decreaseProductQuantity")
                .then()
                .statusCode(200);

        ShoppingCartDto shoppingCartDto = getShoppingCartDto();
        assertNotNull(shoppingCartDto);
        assertTrue(shoppingCartDto.getSelectedProducts()
                                  .isEmpty());
        assertTrue(shoppingCartDto.isEmpty());
    }

}
