package ma.aui.sse.it.xcommerce.monolithic.controllers;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import ma.aui.sse.it.xcommerce.monolithic.data.dtos.OrderDto;
import ma.aui.sse.it.xcommerce.monolithic.data.entities.OrderStatus;
import ma.aui.sse.it.xcommerce.monolithic.mapper.OrderMapper;
import ma.aui.sse.it.xcommerce.monolithic.services.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 *
 * @author Omar IRAQI
 */
@Path("/rest/order")
public class OrderRestController {

    private static final Logger LOG = LoggerFactory.getLogger(OrderRestController.class);

    @Inject
    private OrderService orderService;

    @GET
    @Path("/list")
    public List<OrderDto> getOrdersByCustomer() {
        LOG.debug("getOrdersByCustomer : customerId={}", 1);
        //Retrieve customerId from JWT
        long customerId = 1; //To be removed
        return orderService.getOrdersByCustomer(customerId);
    }

    @GET
    @Path("/checkout")
    public void checkout() {
        LOG.debug("checkout : customerId={}", 1);
        //Retrieve customerId from JWT
        long customerId = 1; //To be removed
        orderService.checkout(customerId);
    }

    @GET
    @Path("/backOffice/list")
    public List<OrderDto> getOrdersByCustomer(@QueryParam("customerId") long customerId) {
        LOG.debug("getOrdersByCustomer(backOffice) : customerId={}", customerId);
        return orderService.getOrdersByCustomer(customerId);
    }

    @GET
    @Path("/backOffice/updateStatus")
    public void updateOrderStatus(@QueryParam("orderId") long orderId,
                                  @QueryParam("newStatus") int newStatus) {
        LOG.debug("updateOrderStatus : orderId={}, newStatus={}", orderId, newStatus);
        switch (newStatus) {
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
