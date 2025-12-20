package ma.aui.sse.it.xcommerce.monolithic.data.dtos;

import java.io.Serial;
import java.io.Serializable;

public class ProductCartDto implements Serializable{

    @Serial
    private static final long serialVersionUID = 781803052368469398L;
    private long id;
    private int quantity;

    public ProductCartDto() { }

    public long getId() {
        return id;
    }

    public void setProductId(long id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantiy) {
        this.quantity = quantiy;
    }
}