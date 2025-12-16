package ma.aui.sse.it.xcommerce.monolithic.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ma.aui.sse.it.xcommerce.monolithic.data.dtos.OrderDto;
import ma.aui.sse.it.xcommerce.monolithic.data.dtos.ProductDto;
import ma.aui.sse.it.xcommerce.monolithic.data.entities.*;
import ma.aui.sse.it.xcommerce.monolithic.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ma.aui.sse.it.xcommerce.monolithic.data.dtos.ShoppingCartDto;
import ma.aui.sse.it.xcommerce.monolithic.data.repositories.UserRepository;
import ma.aui.sse.it.xcommerce.monolithic.data.repositories.OrderRepository;

/**
 *
 * @author Omar IRAQI
 */
@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private OrderMapper orderMapper;

    public List<OrderDto> getOrdersByCustomer(long customerId) {
        List<Order> orderList = orderRepository.findByCustomer(customerId);
        return orderMapper.toOrderDto(orderList);
    }

    public void checkout(long userId) {
        User user = userRepository.findById(userId)
                                  .get();
        ShoppingCartDto shoppingCartDto = shoppingCartService.getShoppingCart(userId);
        if (shoppingCartDto == null || shoppingCartDto.getProductsTotalPrice() == 0)
            return;

        Order order = createNewOrder(shoppingCartDto, user);
        orderRepository.save(order);
        shoppingCartService.empty(shoppingCartDto, user.getId());
    }

    public Order createNewOrder(ShoppingCartDto shoppingCartDto, User user) {
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
        Order order = orderRepository.findById(orderId)
                                     .get();
        order.updateStatus(newStatus);
        orderRepository.save(order);
    }
}