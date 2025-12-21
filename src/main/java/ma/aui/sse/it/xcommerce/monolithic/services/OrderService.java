package ma.aui.sse.it.xcommerce.monolithic.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import ma.aui.sse.it.xcommerce.monolithic.data.dtos.OrderDto;
import ma.aui.sse.it.xcommerce.monolithic.data.dtos.ProductDto;
import ma.aui.sse.it.xcommerce.monolithic.data.entities.*;
import ma.aui.sse.it.xcommerce.monolithic.mapper.OrderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ma.aui.sse.it.xcommerce.monolithic.data.dtos.ShoppingCartDto;
import ma.aui.sse.it.xcommerce.monolithic.data.repositories.UserRepository;
import ma.aui.sse.it.xcommerce.monolithic.data.repositories.OrderRepository;

/**
 *
 * @author Omar IRAQI
 */
@ApplicationScoped
public class OrderService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderService.class);

    @Inject
    private OrderRepository orderRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private ShoppingCartService shoppingCartService;

    @Inject
    private OrderMapper orderMapper;

    public List<OrderDto> getOrdersByCustomer(long customerId) {
        LOG.debug("Lecture des commandes du client {}", customerId);
        List<Order> orderList = orderRepository.findByCustomer(customerId);
        return orderMapper.toOrderDto(orderList);
    }

    public void checkout(long userId) {
        LOG.debug("Validation du panier pour l'utilisateur {}", userId);
        User user = userRepository.findById(userId)
                                  .get();
        ShoppingCartDto shoppingCartDto = shoppingCartService.getShoppingCart(userId);
        if (shoppingCartDto == null || shoppingCartDto.getProductsTotalPrice() == 0) {
            LOG.debug("Aucune commande creee pour l'utilisateur {} (panierNull={}, total={})", userId,
                      shoppingCartDto == null,
                      shoppingCartDto != null ? shoppingCartDto.getProductsTotalPrice() : null);
            return;
        }

        Order order = createNewOrder(shoppingCartDto, user);
        orderRepository.save(order);
        shoppingCartService.empty(shoppingCartDto, user.getId());
    }

    public Order createNewOrder(ShoppingCartDto shoppingCartDto, User user) {
        LOG.debug("Creation d'une nouvelle commande (userId={}, panierNull={})",
                  user != null ? user.getId() : null,
                  shoppingCartDto == null);
        final Order order = new Order();
        order.setUser(user);

        final List<OrderLine> orderLines = new ArrayList<>();
        if (shoppingCartDto.getSelectedProducts() != null) {
            Iterator<Map.Entry<ProductDto, Integer>> it = shoppingCartDto.getSelectedProducts()
                                                                         .entrySet()
                                                                         .iterator();
            while (it.hasNext()) {
                Map.Entry<ProductDto, Integer> e = it.next();
                final Product product = new Product();
                product.setId(e.getKey()
                               .getId());
                OrderLine line = new OrderLine(order, product, e.getValue());
                orderLines.add(line);
            }
        }
        order.setOrderLines(orderLines);
        order.setProductsTotalPrice(shoppingCartDto.getProductsTotalPrice());
        order.setShippingCost(shoppingCartDto.getShippingCost());
        order.setStatus(OrderStatus.HANDLING);
        return order;
    }

    public void updateOrderStatus(long orderId, OrderStatus newStatus) {
        LOG.debug("Mise a jour du statut de commande {} vers {}", orderId, newStatus);
        Order order = orderRepository.findById(orderId)
                                     .get();
        order.updateStatus(newStatus);
        orderRepository.save(order);
    }
}
