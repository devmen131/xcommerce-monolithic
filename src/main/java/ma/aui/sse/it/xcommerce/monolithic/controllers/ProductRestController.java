package ma.aui.sse.it.xcommerce.monolithic.controllers;

import ma.aui.sse.it.xcommerce.monolithic.data.dtos.ProductDto;
import ma.aui.sse.it.xcommerce.monolithic.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/product")
public class ProductRestController {

    private static final Logger LOG = LoggerFactory.getLogger(ProductRestController.class);

    private final ProductService productService;

    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public ProductDto getProductById(@PathVariable Long id) {
        LOG.debug("getProductById : id={}", id);
        return productService.getProductById(id)
                             .orElseThrow(() -> new RuntimeException("Product Not Found!"));
    }
}
