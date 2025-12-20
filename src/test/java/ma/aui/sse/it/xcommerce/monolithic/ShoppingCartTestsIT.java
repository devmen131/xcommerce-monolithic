package ma.aui.sse.it.xcommerce.monolithic;

import ma.aui.sse.it.xcommerce.monolithic.data.dtos.ProductCartDto;
import ma.aui.sse.it.xcommerce.monolithic.data.dtos.ProductDto;
import ma.aui.sse.it.xcommerce.monolithic.data.dtos.ShoppingCartDto;
import org.junit.Test;
import org.springframework.http.MediaType;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ShoppingCartTestsIT extends AbstractTestIT {

    @Test
    public void givenOneProduct_WhenAddToCart_thenNumberOfProductIsOne() throws Exception {
        //Given
        ProductCartDto productCartDtoIphoneX = getProductCartDtoIphoneX(2);

        //When
        addProduct(productCartDtoIphoneX)
                .andExpect(status().isOk());

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
                .andExpect(status().isOk());
        addProduct(productCartDtoS10)
                .andExpect(status().isOk());

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
                .andExpect(status().isOk());
        addProduct(productCartDtoS10)
                .andExpect(status().isOk());

        //When
        mockMvc.perform(patch("/rest/shoppingCart/removeProduct")
                       .accept(MediaType.APPLICATION_JSON)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(convertToJson(productCartDtoS10)))
               .andExpect(status().isOk());

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
                .andExpect(status().isOk());
        productCartDtoS10.setQuantity(2);
        addProduct(productCartDtoS10)
                .andExpect(status().isOk());

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
        addProduct(productCartDtoS10).andExpect(status().isOk());

        productCartDtoS10.setQuantity(1);
        mockMvc.perform(patch("/rest/shoppingCart/decreaseProductQuantity")
                       .contentType(MediaType.APPLICATION_JSON)
                       .accept(MediaType.APPLICATION_JSON)
                       .content(convertToJson(productCartDtoS10)))
               .andExpect(status().isOk());

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
        addProduct(productCartDtoS10).andExpect(status().isOk());

        mockMvc.perform(patch("/rest/shoppingCart/decreaseProductQuantity")
                       .contentType(MediaType.APPLICATION_JSON)
                       .accept(MediaType.APPLICATION_JSON)
                       .content(convertToJson(productCartDtoS10)))
               .andExpect(status().isOk());

        ShoppingCartDto shoppingCartDto = getShoppingCartDto();
        assertNotNull(shoppingCartDto);
        assertTrue(shoppingCartDto.getSelectedProducts()
                                  .isEmpty());
        assertTrue(shoppingCartDto.isEmpty());
    }

}
