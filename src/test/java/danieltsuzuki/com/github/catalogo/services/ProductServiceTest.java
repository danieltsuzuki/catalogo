package danieltsuzuki.com.github.catalogo.services;

import danieltsuzuki.com.github.catalogo.dto.ProductDTO;
import danieltsuzuki.com.github.catalogo.entities.Category;
import danieltsuzuki.com.github.catalogo.entities.Product;
import danieltsuzuki.com.github.catalogo.repositories.CategoryRepository;
import danieltsuzuki.com.github.catalogo.repositories.ProductRepository;
import danieltsuzuki.com.github.catalogo.services.exceptions.DatabaseException;
import danieltsuzuki.com.github.catalogo.services.exceptions.ResourceNotFoundException;
import danieltsuzuki.com.github.catalogo.utils.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository repository;

    @Mock
    private CategoryRepository categoryRepository;

    private Long existingId;
    private Long dontExistingId;
    private Long dependentId;
    private PageImpl<Product> page;
    private Product product;
    private ProductDTO productDTO;
    private Category category;

    @BeforeEach
    public void setUp(){
        existingId = 1l;
        dontExistingId = 1000l;
        dependentId = 4l;
        product = Factory.createProduct();
        category = Factory.createCategory();
        productDTO = Factory.createProductDTO();
        page = new PageImpl<>(List.of(product));

        when(repository.findAll((Pageable) any())).thenReturn(page);
        when(repository.getReferenceById(existingId)).thenReturn(product);
        when(categoryRepository.getReferenceById(existingId)).thenReturn(category);
        when(repository.save(any())).thenReturn(product);
        when(repository.findById(existingId)).thenReturn(Optional.of(product));
        when(repository.findById(dontExistingId)).thenReturn(Optional.empty());

        when(repository.existsById(existingId)).thenReturn(true);
        when(repository.existsById(dependentId)).thenReturn(true);


        doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
        doThrow(ResourceNotFoundException.class).when(repository).findById(dontExistingId);
        doThrow(ResourceNotFoundException.class).when(repository).getReferenceById(dontExistingId);
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() {
        assertDoesNotThrow(() -> {
            service.delete(existingId);
        });

        verify(repository, times(1)).deleteById(existingId);
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDontExist() {
        var e = assertThrows(ResourceNotFoundException.class ,() -> {
            service.delete(dontExistingId);
        });

        assertEquals("ID not found " + dontExistingId,e.getMessage());
        assertNotNull(e);
        assertEquals(ResourceNotFoundException.class, e.getClass());
    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenDependentId() {
        var e = assertThrows(DatabaseException.class ,() -> {
            service.delete(dependentId);
        });

        assertEquals("Integrity violation",e.getMessage());
        assertNotNull(e);
        assertEquals(DatabaseException.class, e.getClass());
    }

    @Test
    public void findAllShouldReturnPage(){
        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductDTO> result = service.findAllPaged(pageable);

        assertNotNull(result);
        verify(repository, times(1)).findAll(pageable);
    }

    @Test
    public void findByIdShouldReturnProductDTOWhenIdExists() {
        ProductDTO dto = service.findById(existingId);

        assertNotNull(dto);
        assertEquals(existingId, dto.getId());
        assertEquals(productDTO.getName(), dto.getName());
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDontExist() {
        var e = assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(dontExistingId);
        });

        assertNotNull(e);
        assertEquals(ResourceNotFoundException.class, e.getClass());
    }

    @Test
    public void updateShouldReturnProductDTOWhenIdExists() {
        ProductDTO dto = service.update(existingId, productDTO);

        assertNotNull(dto);
        assertEquals(productDTO.getClass(), dto.getClass());
    }

    @Test
    public void updateShouldThrowRecourceNotFoundExceptionWhenIdDontExist() {
       var e = assertThrows(ResourceNotFoundException.class, () -> {
           service.update(dontExistingId, productDTO);
       });

        assertNotNull(e);
        assertEquals(ResourceNotFoundException.class, e.getClass());
    }

}