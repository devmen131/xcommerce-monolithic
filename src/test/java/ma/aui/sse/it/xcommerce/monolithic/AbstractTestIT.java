package ma.aui.sse.it.xcommerce.monolithic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import ma.aui.sse.it.xcommerce.monolithic.data.dtos.OrderDto;
import ma.aui.sse.it.xcommerce.monolithic.data.dtos.ProductCartDto;
import ma.aui.sse.it.xcommerce.monolithic.data.dtos.ProductDto;
import ma.aui.sse.it.xcommerce.monolithic.data.dtos.ShoppingCartDto;
import ma.aui.sse.it.xcommerce.monolithic.services.ShoppingCartService;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@RunWith(SpringRunner.class)
public abstract class AbstractTestIT {

    public static final int USER_ID = 1;
    protected static final long PRODUCT_ID_S10 = 1;
    protected static final long PRODUCT_ID_IPHONE_X = 2;
    protected final ObjectMapper objectMapper = new ObjectMapper();
    private final ObjectWriter writer = objectMapper.writer()
                                                    .withDefaultPrettyPrinter();

    @Autowired
    protected StringRedisTemplate stringRedisTemplate;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ShoppingCartService shoppingCartService;

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

    @Before
    @After
    public void setup() {
        clearCache();
    }

    private void clearCache() {
        stringRedisTemplate.keys("*")
                           .forEach(s -> stringRedisTemplate.delete(s));
    }

    protected String convertToJson(Object object) throws JsonProcessingException {
        return writer.writeValueAsString(object);
    }

    protected ResultActions addProduct(ProductCartDto productCartDtoIphoneX) throws Exception {
        return mockMvc.perform(patch("/rest/shoppingCart/addProduct")
                .content(convertToJson(productCartDtoIphoneX))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));
    }

    protected ProductDto getProductDto(long productId) throws Exception {
        String productStr = mockMvc.perform(get("/rest/product/" + productId))
                                   .andExpect(status().isOk())
                                   .andReturn()
                                   .getResponse()
                                   .getContentAsString();
        return objectMapper.readValue(productStr, new TypeReference<ProductDto>() {});
    }

    public ShoppingCartDto getShoppingCartDto() throws Exception {
        String shoppingCartStr = mockMvc.perform(get("/rest/shoppingCart/get"))
                                        .andExpect(status().isOk())
                                        .andReturn()
                                        .getResponse()
                                        .getContentAsString();
        return objectMapper.readValue(shoppingCartStr, new TypeReference<ShoppingCartDto>() {});
    }

    protected List<OrderDto> getOrdersByCustomer(int userId) throws Exception {
        String ordersStr = mockMvc.perform(get("/rest/order/backOffice/list")
                                          .param("customerId", String.valueOf(userId)))
                                  .andExpect(status().isOk())
                                  .andReturn()
                                  .getResponse()
                                  .getContentAsString();

        return objectMapper.readValue(ordersStr, new TypeReference<List<OrderDto>>() {});
    }
}
