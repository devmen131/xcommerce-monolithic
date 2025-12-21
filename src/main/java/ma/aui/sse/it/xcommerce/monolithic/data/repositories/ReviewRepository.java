package ma.aui.sse.it.xcommerce.monolithic.data.repositories;

import ma.aui.sse.it.xcommerce.monolithic.data.entities.Review;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Omar IRAQI
 */
@Repository
public interface ReviewRepository extends BaseRepository<Review>{
    @Query("select r from Review r where r.product.id = ?1")
    List<Review> findByProduct(long productId);
}