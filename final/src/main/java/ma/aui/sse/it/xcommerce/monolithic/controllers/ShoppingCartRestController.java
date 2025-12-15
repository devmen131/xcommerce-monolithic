package ma.aui.sse.it.xcommerce.monolithic.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.test.context.support.WithMockUser;

import ma.aui.sse.it.xcommerce.monolithic.data.dtos.ProductCartDto;
import ma.aui.sse.it.xcommerce.monolithic.data.dtos.ShoppingCartDto;
import ma.aui.sse.it.xcommerce.monolithic.services.ShoppingCartService;

/**
 *
 * @author Omar IRAQI
 */
@RestController
@RequestMapping("/rest/shoppingCart")
@WithMockUser
public class ShoppingCartRestController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @GetMapping("/get")
    public ShoppingCartDto getShoppingCart(/* Authentication auth */){
        //Retrieve userId from JWT-based security context
        //auth.getPrinciple()
        long userId = 1; //To be removed
        System.out.println(shoppingCartService);
        return shoppingCartService.getShoppingCart(userId);
    }

    @PatchMapping("/addProduct")
    public ShoppingCartDto addProduct(@RequestBody ProductCartDto dto){
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
        //Retrieve userId from JWT-based security context
        //auth.getPrinciple()
        long userId = 1; //To be removed
        ShoppingCartDto shoppingCartDto = shoppingCartService.getShoppingCart(userId);
        return shoppingCartService.decreaseProductQuantity(shoppingCartDto, userId, dto.getId(),
                                                    dto.getQuantity());
    }

    @PatchMapping("/removeProduct")
    public ShoppingCartDto removeProduct(@RequestBody ProductCartDto dto){
        //Retrieve userId from JWT-based security context
        //auth.getPrinciple()
        long userId = 1; //To be removed
        ShoppingCartDto shoppingCartDto = shoppingCartService.getShoppingCart(userId);
        return shoppingCartService.removeProduct(shoppingCartDto, userId, dto.getId());
    }
}