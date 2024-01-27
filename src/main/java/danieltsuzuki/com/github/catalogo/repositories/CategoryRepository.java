package danieltsuzuki.com.github.catalogo.repositories;

import danieltsuzuki.com.github.catalogo.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
