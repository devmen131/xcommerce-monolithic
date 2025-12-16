package ma.aui.sse.it.xcommerce.monolithic.data.dtos;


import java.io.Serializable;
import java.util.Hashtable;


/**
 *
 * @author Omar IRAQI
 */
public class ShoppingCartDto implements Serializable {

    private static final long serialVersionUID = 1718078099996510259L;
    private Hashtable<ProductDto, Integer> selectedProducts;
    private float productsTotalPrice;
    private float shippingCost;
    private static final float BASE_SHIPPING_COST = 25;

    public ShoppingCartDto() {
        selectedProducts = new Hashtable<>();
        productsTotalPrice = 0;
        shippingCost = 0;
    }

    public float getProductsTotalPrice() {
        return productsTotalPrice;
    }

    public float getShippingCost() {
        return shippingCost;
    }

    public float getTotalPrice() {
        return productsTotalPrice + ((shippingCost == 0) ? 0 : shippingCost + BASE_SHIPPING_COST);
    }

    public boolean isEmpty() {
        return productsTotalPrice == 0;
    }

    public void empty() {
        selectedProducts = new Hashtable<>();
        productsTotalPrice = 0;
        shippingCost = 0;
    }

    public void setSelectedProducts(Hashtable<ProductDto, Integer> selectedProducts) {
        this.selectedProducts = selectedProducts;
    }

    public void setProductsTotalPrice(float productsTotalPrice) {
        this.productsTotalPrice = productsTotalPrice;
    }

    public void setShippingCost(float shippingCost) {
        this.shippingCost = shippingCost;
    }

    public Hashtable<ProductDto, Integer> getSelectedProducts() {
        return selectedProducts;
    }
}
