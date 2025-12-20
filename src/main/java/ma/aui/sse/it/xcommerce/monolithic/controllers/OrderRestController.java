package ma.aui.sse.it.xcommerce.monolithic.controllers;

import ma.aui.sse.it.xcommerce.monolithic.data.dtos.OrderDto;
import ma.aui.sse.it.xcommerce.monolithic.data.entities.OrderStatus;
import ma.aui.sse.it.xcommerce.monolithic.mapper.OrderMapper;
import ma.aui.sse.it.xcommerce.monolithic.services.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * @author Omar IRAQI
 */
@RestController
@RequestMapping("/rest/order")
public class OrderRestController {

    private static final Logger LOG = LoggerFactory.getLogger(OrderRestController.class);

    @Autowired
    private OrderService orderService;

    @GetMapping("/list")
    public List<OrderDto> getOrdersByCustomer() {
        LOG.debug("getOrdersByCustomer : customerId={}", 1);
        //Retrieve customerId from JWT
        long customerId = 1; //To be removed
        return orderService.getOrdersByCustomer(customerId);
    }

    @GetMapping("/checkout")
    public void checkout() {
        LOG.debug("checkout : customerId={}", 1);
        //Retrieve customerId from JWT
        long customerId = 1; //To be removed
        orderService.checkout(customerId);
    }

    @GetMapping("/backOffice/list")
    public List<OrderDto> getOrdersByCustomer(@RequestParam long customerId) {
        LOG.debug("getOrdersByCustomer(backOffice) : customerId={}", customerId);
        return orderService.getOrdersByCustomer(customerId);
    }

    @GetMapping("/backOffice/updateStatus")
    public void updateOrderStatus(@RequestParam long orderId, @RequestParam int newStatus){
        LOG.debug("updateOrderStatus : orderId={}, newStatus={}", orderId, newStatus);
        switch(newStatus){
            case 1:
                orderService.updateOrderStatus(orderId, OrderStatus.SHIPPED);
                break;
            case 2:
                orderService.updateOrderStatus(orderId, OrderStatus.DELIVERED);
                break;
            case 3:
                orderService.updateOrderStatus(orderId, OrderStatus.ONHOLD);
                break;
            case 4:
                orderService.updateOrderStatus(orderId, OrderStatus.CANCELED);
                break;
        }        
    }
}
