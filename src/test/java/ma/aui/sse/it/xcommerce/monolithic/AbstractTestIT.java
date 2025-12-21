package ma.aui.sse.it.xcommerce.monolithic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.quarkus.redis.client.RedisClient;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import jakarta.inject.Inject;
import ma.aui.sse.it.xcommerce.monolithic.data.dtos.OrderDto;
import ma.aui.sse.it.xcommerce.monolithic.data.dtos.ProductCartDto;
import ma.aui.sse.it.xcommerce.monolithic.data.dtos.ProductDto;
import ma.aui.sse.it.xcommerce.monolithic.data.dtos.ShoppingCartDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.given;

public abstract class AbstractTestIT {

    public static final int USER_ID = 1;
    protected static final long PRODUCT_ID_S10 = 1;
    protected static final long PRODUCT_ID_IPHONE_X = 2;
    protected final ObjectMapper objectMapper = new ObjectMapper();
    private final ObjectWriter writer = objectMapper.writer()
                                                    .withDefaultPrettyPrinter();

    @Inject
    RedisClient redisClient;

    protected static ProductCartDto getProductCartDtoIphoneX(int quantity) {
        return getProductCartDto(PRODUCT_ID_IPHONE_X, quantity);
    }

    protected static ProductCartDto getProductCartDtoS10(int quantity) {
        return getProductCartDto(PRODUCT_ID_S10, quantity);
    }

    private static ProductCartDto getProductCartDto(long productId, int quantity) {
        final ProductCartDto productCartDto = new ProductCartDto();
        productCartDto.setProductId(productId);
        productCartDto.setQuantity(quantity);
        return productCartDto;
    }

    @BeforeEach
    @AfterEach
    public void setup() {
        clearCache();
    }

    private void clearCache() {
        redisClient.flushdb(Collections.emptyList());
    }

    protected String convertToJson(Object object) throws JsonProcessingException {
        return writer.writeValueAsString(object);
    }

    protected ValidatableResponse addProduct(ProductCartDto productCartDto) throws Exception {
        return given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(convertToJson(productCartDto))
                .when()
                .patch("/rest/shoppingCart/addProduct")
                .then();
    }

    protected ProductDto getProductDto(long productId) throws Exception {
        String productStr = given()
                .when()
                .get("/rest/product/" + productId)
                .then()
                .statusCode(200)
                .extract()
                .asString();
        return objectMapper.readValue(productStr, new TypeReference<ProductDto>() {});
    }

    public ShoppingCartDto getShoppingCartDto() throws Exception {
        String shoppingCartStr = given()
                .when()
                .get("/rest/shoppingCart/get")
                .then()
                .statusCode(200)
                .extract()
                .asString();
        return objectMapper.readValue(shoppingCartStr, new TypeReference<ShoppingCartDto>() {});
    }

    protected List<OrderDto> getOrdersByCustomer(int userId) throws Exception {
        String ordersStr = given()
                .queryParam("customerId", String.valueOf(userId))
                .when()
                .get("/rest/order/backOffice/list")
                .then()
                .statusCode(200)
                .extract()
                .asString();

        return objectMapper.readValue(ordersStr, new TypeReference<>() {});
    }
}
