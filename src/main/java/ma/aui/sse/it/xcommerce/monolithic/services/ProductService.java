package ma.aui.sse.it.xcommerce.monolithic.services;

import ma.aui.sse.it.xcommerce.monolithic.data.dtos.ProductDto;
import ma.aui.sse.it.xcommerce.monolithic.data.repositories.ProductRepository;
import ma.aui.sse.it.xcommerce.monolithic.mapper.ProductMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductService.class);

    private final ProductMapper productMapper;
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public Optional<ProductDto> getProductById(long id) {
        LOG.debug("Recherche du produit {}", id);
        return productRepository.findById(id)
                                .map(productMapper::toProductDto);
    }
}
