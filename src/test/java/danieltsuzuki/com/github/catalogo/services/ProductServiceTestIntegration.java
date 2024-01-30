package danieltsuzuki.com.github.catalogo.services;

import danieltsuzuki.com.github.catalogo.dto.ProductDTO;
import danieltsuzuki.com.github.catalogo.repositories.ProductRepository;
import danieltsuzuki.com.github.catalogo.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ProductServiceTestIntegration {

    @Autowired
    private ProductService service;

    @Autowired
    private ProductRepository repository;

    private Long existingId;
    private Long dontExistingId;
    private Long dependentId;
    private Long countTotalProducts;

    @BeforeEach
    public void setUp(){
        existingId = 1l;
        dontExistingId = 1000l;
        countTotalProducts = 25l;
        dependentId = 4l;
    }

    @Test
    public void deleteShouldDeleteResourceWhenIdExists() {
        service.delete(existingId);

        assertEquals(countTotalProducts -1, repository.count());
        assertEquals(Optional.empty(), repository.findById(existingId));
    }

    @Test
    public void deleteShouldThrowRecourceNotFoundExceptionWhenIdDontExist() {
        var e = assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(dontExistingId);
        });

        assertEquals("ID not found " + dontExistingId, e.getMessage());
        assertEquals(ResourceNotFoundException.class, e.getClass());
    }

    @Test
    public void findAllPagedShouldReturnPageWhenPage0Size10(){
        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductDTO> result = service.findAllPaged(pageable);

        assertFalse(result.isEmpty());
        assertEquals(0, result.getNumber());
        assertEquals(10, result.getSize());
        assertEquals(countTotalProducts, result.getTotalElements());
    }

    @Test
    public void findAllPagedShouldReturnEmptyPageWhenPageDontExtist(){
        Pageable pageable = PageRequest.of(50, 10);
        Page<ProductDTO> result = service.findAllPaged(pageable);

        assertTrue(result.isEmpty());
    }

    @Test
    public void findAllPagedShouldReturnSortedPageWhenSortedByName(){
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));
        Page<ProductDTO> result = service.findAllPaged(pageable);

        assertFalse(result.isEmpty());
        assertEquals("Macbook Pro", result.getContent().get(0).getName());
        assertEquals("PC Gamer", result.getContent().get(1).getName());
        assertEquals("PC Gamer Alfa", result.getContent().get(2).getName());
    }


}