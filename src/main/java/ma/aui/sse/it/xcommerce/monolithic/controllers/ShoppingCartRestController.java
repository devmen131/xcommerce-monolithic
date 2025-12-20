package ma.aui.sse.it.xcommerce.monolithic.controllers;

import ma.aui.sse.it.xcommerce.monolithic.data.dtos.ProductCartDto;
import ma.aui.sse.it.xcommerce.monolithic.data.dtos.ShoppingCartDto;
import ma.aui.sse.it.xcommerce.monolithic.services.ShoppingCartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Omar IRAQI
 */
@RestController
@RequestMapping("/rest/shoppingCart")
@WithMockUser
public class ShoppingCartRestController {

    private static final Logger LOG = LoggerFactory.getLogger(ShoppingCartRestController.class);

    @Autowired
    private ShoppingCartService shoppingCartService;

    @GetMapping("/get")
    public ShoppingCartDto getShoppingCart(/* Authentication auth */){
        LOG.debug("getShoppingCart : userId={}", 1);
        //Retrieve userId from JWT-based security context
        //auth.getPrinciple()
        long userId = 1; //To be removed
        return shoppingCartService.getShoppingCart(userId);
    }

    @PatchMapping("/addProduct")
    public ShoppingCartDto addProduct(@RequestBody ProductCartDto dto){
        Long productId = dto != null ? dto.getId() : null;
        Integer quantity = dto != null ? dto.getQuantity() : null;
        LOG.debug("addProduct : productId={}, quantity={}", productId, quantity);
        //Retrieve userId from JWT-based security context
        //auth.getPrinciple()
        long userId = 1; //To be removed
        ShoppingCartDto shoppingCartDto = shoppingCartService.getShoppingCart(userId);
        return shoppingCartService.addProduct(shoppingCartDto, userId,
                                                dto.getId(),
                                                dto.getQuantity());
    }

    @PatchMapping("/decreaseProductQuantity")
    public ShoppingCartDto decreaseProductQuantity(@RequestBody ProductCartDto dto){
        Long productId = dto != null ? dto.getId() : null;
        Integer quantity = dto != null ? dto.getQuantity() : null;
        LOG.debug("decreaseProductQuantity : productId={}, quantity={}", productId, quantity);
        //Retrieve userId from JWT-based security context
        //auth.getPrinciple()
        long userId = 1; //To be removed
        ShoppingCartDto shoppingCartDto = shoppingCartService.getShoppingCart(userId);
        return shoppingCartService.decreaseProductQuantity(shoppingCartDto, userId, dto.getId(),
                                                    dto.getQuantity());
    }

    @PatchMapping("/removeProduct")
    public ShoppingCartDto removeProduct(@RequestBody ProductCartDto dto){
        Long productId = dto != null ? dto.getId() : null;
        LOG.debug("removeProduct : productId={}", productId);
        //Retrieve userId from JWT-based security context
        //auth.getPrinciple()
        long userId = 1; //To be removed
        ShoppingCartDto shoppingCartDto = shoppingCartService.getShoppingCart(userId);
        return shoppingCartService.removeProduct(shoppingCartDto, userId, dto.getId());
    }
}
