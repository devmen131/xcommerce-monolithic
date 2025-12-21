package ma.aui.sse.it.xcommerce.monolithic.controllers;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import ma.aui.sse.it.xcommerce.monolithic.data.dtos.ProductDto;
import ma.aui.sse.it.xcommerce.monolithic.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/rest/product")
public class ProductRestController {

    private static final Logger LOG = LoggerFactory.getLogger(ProductRestController.class);

    private final ProductService productService;

    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @GET
    @Path("/{id}")
    public ProductDto getProductById(@PathParam("id") Long id) {
        LOG.debug("getProductById : id={}", id);
        return productService.getProductById(id)
                             .orElseThrow(() -> new RuntimeException("Product Not Found!"));
    }
}
