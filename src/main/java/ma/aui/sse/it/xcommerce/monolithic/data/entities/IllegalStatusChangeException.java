package ma.aui.sse.it.xcommerce.monolithic.data.entities;

import java.io.Serial;

/**
 *
 * @author Omar IRAQI
 */
public class IllegalStatusChangeException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -199006397514123367L;
    private OrderStatus problematicStatus;
    
    public IllegalStatusChangeException() {
        super();
    }

    public IllegalStatusChangeException(OrderStatus problematicStatus) {
        super();
        this.problematicStatus = problematicStatus;
    }

    public OrderStatus getProblematicStatus() {
        return problematicStatus;
    }
}
