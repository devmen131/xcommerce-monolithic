package ma.aui.sse.it.xcommerce.monolithic.data.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * @author Omar IRAQI
 */
@Entity
public class Brand extends BaseEntity {

    @NotNull
    private String name;

    @OneToMany(mappedBy = "brand", fetch = FetchType.LAZY)
    @OrderBy("name asc")
    private List<Product> products;

    protected Brand() {
    }

    public Brand(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the products
     */
    public List<Product> getProducts() {
        return products;
    }

    /**
     * @param products the products to set
     */
    public void setProducts(List<Product> products) {
        this.products = products;
    }
}