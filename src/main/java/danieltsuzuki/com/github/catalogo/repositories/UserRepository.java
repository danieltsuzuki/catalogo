package danieltsuzuki.com.github.catalogo.repositories;

import danieltsuzuki.com.github.catalogo.entities.Category;
import danieltsuzuki.com.github.catalogo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
