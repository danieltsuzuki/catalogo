package danieltsuzuki.com.github.catalogo.repositories;

import danieltsuzuki.com.github.catalogo.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
