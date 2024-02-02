package danieltsuzuki.com.github.catalogo.repositories;

import danieltsuzuki.com.github.catalogo.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
