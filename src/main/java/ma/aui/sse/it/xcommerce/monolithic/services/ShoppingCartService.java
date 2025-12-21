package ma.aui.sse.it.xcommerce.monolithic.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import ma.aui.sse.it.xcommerce.monolithic.data.dtos.ProductDto;
import ma.aui.sse.it.xcommerce.monolithic.data.dtos.ShoppingCartDto;
import ma.aui.sse.it.xcommerce.monolithic.data.entities.Product;
import ma.aui.sse.it.xcommerce.monolithic.data.repositories.ProductRepository;
import ma.aui.sse.it.xcommerce.monolithic.mapper.ProductMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author Omar IRAQI
 */
@ApplicationScoped
public class ShoppingCartService {

    private static final Logger LOG = LoggerFactory.getLogger(ShoppingCartService.class);

    @Inject
    private ProductRepository productRepository;

    @Inject
    private ProductMapper productMapper;
    
    @Cacheable(value = "ShoppingCartDto")
    public ShoppingCartDto getShoppingCart(long userId) {
        LOG.debug("Lecture du panier de l'utilisateur {}", userId);
        return new ShoppingCartDto();
    }

    @CachePut(value = "ShoppingCartDto", key = "#userId")
    public ShoppingCartDto addProduct(ShoppingCartDto shoppingCartDto, long userId, long productId, int quantity) {
        LOG.debug("Ajout du produit {} dans le panier de l'utilisateur {} (quantite={}, panierNull={})", productId, userId,
                  quantity, shoppingCartDto == null);
        Product product = productRepository.findById(productId)
                                           .get();
        ProductDto productDto = productMapper.toProductDto(product);
        addProduct(shoppingCartDto, productDto, quantity);
        return shoppingCartDto;
    }

    @CachePut(value = "ShoppingCartDto", key = "#userId")
    public ShoppingCartDto decreaseProductQuantity(ShoppingCartDto shoppingCartDto, long userId, long productId, int quantity) {
        LOG.debug("Diminution de quantite du produit {} dans le panier de l'utilisateur {} (quantite={}, panierNull={})",
                  productId, userId, quantity, shoppingCartDto == null);
        Product product = productRepository.findById(productId)
                                           .get();
        ProductDto productDto = productMapper.toProductDto(product);
        removeProduct(shoppingCartDto, productDto, quantity);
        return shoppingCartDto;
    }

    @CachePut(value = "ShoppingCartDto", key = "#userId")
    public ShoppingCartDto removeProduct(ShoppingCartDto shoppingCartDto, long userId, long productId) {
        LOG.debug("Suppression du produit {} du panier de l'utilisateur {} (panierNull={})", productId, userId,
                  shoppingCartDto == null);
        Product product = productRepository.findById(productId)
                                           .get();
        ProductDto productDto = productMapper.toProductDto(product);
        removeProduct(shoppingCartDto, productDto);
        return shoppingCartDto;
    }

    @CachePut(value = "ShoppingCartDto", key = "#userId")
    public ShoppingCartDto empty(ShoppingCartDto shoppingCartDto, long userId) {
        LOG.debug("Vidage du panier de l'utilisateur {} (panierNull={})", userId, shoppingCartDto == null);
        shoppingCartDto.empty();
        return shoppingCartDto;
    }

    private void addProduct(ShoppingCartDto shoppingCartDto, ProductDto productDto, int quantity) {
        if (quantity <= 0)
            return;
        updateProductQuantity(shoppingCartDto, productDto, quantity);
    }

    private void removeProduct(ShoppingCartDto shoppingCartDto, ProductDto productDto, int quantity) {
        if (quantity <= 0)
            return;
        updateProductQuantity(shoppingCartDto, productDto, -1 * quantity);
    }

    private void removeProduct(ShoppingCartDto shoppingCartDto, ProductDto productDto) {
        if (shoppingCartDto.getSelectedProducts()
                           .get(productDto) != null)
            removeProduct(shoppingCartDto, productDto, shoppingCartDto.getSelectedProducts()
                                                                      .get(productDto));
    }

    private void updateProductQuantity(ShoppingCartDto shoppingCartDto, ProductDto productDto, int quantity) {
        int currentQuantity = 0;
        if (shoppingCartDto.getSelectedProducts()
                           .get(productDto) != null)
            currentQuantity = shoppingCartDto.getSelectedProducts()
                                             .get(productDto);

        if (quantity + currentQuantity < 0)
            quantity = -1 * currentQuantity;
        if (quantity + currentQuantity == 0)
            shoppingCartDto.getSelectedProducts()
                           .remove(productDto);
        else
            shoppingCartDto.getSelectedProducts()
                           .put(productDto, quantity + currentQuantity);

        //Recompute productsTotalPrice and shippingCost
        //We could just update them, but a discount may be applied by back office meanwhile
        Iterator<Map.Entry<ProductDto, Integer>> it = shoppingCartDto.getSelectedProducts()
                                                                     .entrySet()
                                                                     .iterator();
        shoppingCartDto.setProductsTotalPrice(0);
        shoppingCartDto.setShippingCost(0);
        while (it.hasNext()) {
            Map.Entry<ProductDto, Integer> e = it.next();
            float productsTotalPrice = shoppingCartDto.getProductsTotalPrice();
            productsTotalPrice += e.getKey()
                                   .getPrice() * (1 - e.getKey()
                                                       .getDiscount() / 100) * e.getValue();
            shoppingCartDto.setProductsTotalPrice(productsTotalPrice);
            float shippingCost = shoppingCartDto.getShippingCost();
            shippingCost += e.getKey()
                             .getWeight() * e.getValue();
            shoppingCartDto.setShippingCost(shippingCost);
        }
    }
}
