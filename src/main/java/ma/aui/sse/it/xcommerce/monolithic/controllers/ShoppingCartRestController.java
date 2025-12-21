package ma.aui.sse.it.xcommerce.monolithic.controllers;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.Path;
import ma.aui.sse.it.xcommerce.monolithic.data.dtos.ProductCartDto;
import ma.aui.sse.it.xcommerce.monolithic.data.dtos.ShoppingCartDto;
import ma.aui.sse.it.xcommerce.monolithic.services.ShoppingCartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Omar IRAQI
 */
@Path("/rest/shoppingCart")
public class ShoppingCartRestController {

    private static final Logger LOG = LoggerFactory.getLogger(ShoppingCartRestController.class);

    @Inject
    private ShoppingCartService shoppingCartService;

    @GET
    @Path("/get")
    public ShoppingCartDto getShoppingCart(/* Authentication auth */){
        LOG.debug("getShoppingCart : userId={}", 1);
        //Retrieve userId from JWT-based security context
        //auth.getPrinciple()
        long userId = 1; //To be removed
        return shoppingCartService.getShoppingCart(userId);
    }

    @PATCH
    @Path("/addProduct")
    public ShoppingCartDto addProduct(ProductCartDto dto){
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

    @PATCH
    @Path("/decreaseProductQuantity")
    public ShoppingCartDto decreaseProductQuantity(ProductCartDto dto){
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

    @PATCH
    @Path("/removeProduct")
    public ShoppingCartDto removeProduct(ProductCartDto dto){
        Long productId = dto != null ? dto.getId() : null;
        LOG.debug("removeProduct : productId={}", productId);
        //Retrieve userId from JWT-based security context
        //auth.getPrinciple()
        long userId = 1; //To be removed
        ShoppingCartDto shoppingCartDto = shoppingCartService.getShoppingCart(userId);
        return shoppingCartService.removeProduct(shoppingCartDto, userId, dto.getId());
    }
}
