package danieltsuzuki.com.github.catalogo.repositories;

import danieltsuzuki.com.github.catalogo.entities.Product;
import danieltsuzuki.com.github.catalogo.utils.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository repository;

    private Long existingId;
    private Long dontExistingId;
    private Long countTotalProducts;

    @BeforeEach
    void setUp() {
        existingId = 1l;
        dontExistingId = 1000l;
        countTotalProducts = 25l;
    }


    @Test
    public void deleteShouldDeleteObjectWhenIdExists() {
        repository.deleteById(existingId);

        Optional<Product> result = repository.findById(existingId);

        assertFalse(result.isPresent());
    }

    @Test
    public void saveShouldPersistWithhAutoIncrementWhenIdIsNull() {
        Product product = Factory.createProduct();
        product.setId(null);

        product = repository.save(product);

        assertNotNull(product.getId());
        assertEquals(countTotalProducts + 1, product.getId());
    }

    @Test
    public void findByIdShouldReturnOptionalIsPresentWhenIdExists(){
        Optional<Product> product = repository.findById(existingId);

        assertTrue(product.isPresent());
        assertEquals(existingId, product.get().getId());
    }

    @Test
    public void findByIdShouldReturnOptionalIsEmptyWhenIdDontExists(){
        Optional<Product> product = repository.findById(dontExistingId);

        assertTrue(product.isEmpty());
    }



}