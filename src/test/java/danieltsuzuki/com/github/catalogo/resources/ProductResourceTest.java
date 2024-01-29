package danieltsuzuki.com.github.catalogo.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import danieltsuzuki.com.github.catalogo.dto.ProductDTO;
import danieltsuzuki.com.github.catalogo.entities.Category;
import danieltsuzuki.com.github.catalogo.repositories.CategoryRepository;
import danieltsuzuki.com.github.catalogo.services.ProductService;
import danieltsuzuki.com.github.catalogo.services.exceptions.DatabaseException;
import danieltsuzuki.com.github.catalogo.services.exceptions.ResourceNotFoundException;
import danieltsuzuki.com.github.catalogo.utils.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductResource.class)
class ProductResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService service;

    @MockBean
    private CategoryRepository categoryRepository;

    private PageImpl<ProductDTO> page;

    private ProductDTO productDTO;
    private Long dontExistingId;
    private Long existingId;
    private Long dependentId;
    private Category category;

    @BeforeEach
    public void setUp(){
        existingId = 1l;
        dontExistingId = 1000l;
        dependentId = 4l;
//        product = Factory.createProduct();
        category = Factory.createCategory();
        productDTO = Factory.createProductDTO();
        page = new PageImpl<>(List.of(productDTO));

        when(service.findAllPaged((Pageable) any())).thenReturn(page);
//
        when(service.findById(existingId)).thenReturn(productDTO);
        when(service.findById(dontExistingId)).thenThrow(ResourceNotFoundException.class);

        when(service.update(eq(existingId), any())).thenReturn(productDTO);
        when(service.update(eq(dontExistingId), any())).thenThrow(ResourceNotFoundException.class);

        when(service.insert(any())).thenReturn(productDTO);
        when(categoryRepository.getReferenceById(existingId)).thenReturn(category);

        doNothing().when(service).delete(existingId);
        doThrow(ResourceNotFoundException.class).when(service).delete(dontExistingId);
        doThrow(DatabaseException.class).when(service).delete(dependentId);

    }

    @Test
    public void findAllShouldReturnPage() throws Exception {
        mockMvc.perform(get("/products").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void findByIdShouldReturnProductDTOWhenIdExists() throws Exception {
        mockMvc.perform(get("/products/{id}", existingId).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productDTO.getId()))
                .andExpect(jsonPath("$.name").value(productDTO.getName()));
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdDontExist() throws Exception {
        mockMvc.perform(get("/products/{id}", dontExistingId).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Resource not found"));
    }

    @Test
    public void updateShouldReturnProductDTOWhenIdExists() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(put("/products/{id}", existingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON));
                        //.accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").value(productDTO.getName()));
        result.andExpect(jsonPath("$.price").value(productDTO.getPrice()));
    }

    @Test
    public void updateShouldReturnNotFoundWhenIdDontExist() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(productDTO);

        mockMvc.perform(put("/products/{id}", dontExistingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Resource not found"));
    }

    @Test
    public void deleteShouldReturnNotFoundWhenIdDontExist() throws Exception {
        mockMvc.perform(delete("/products/{id}", dontExistingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Resource not found"));
    }

    @Test
    public void deleteShouldReturnBadRequestWhenIdDontExist() throws Exception {
        mockMvc.perform(delete("/products/{id}", dependentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Database exception"));
    }

    @Test
    public void deleteWhenIdExists() throws Exception {
        mockMvc.perform(delete("/products/{id}", existingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void insertShouldReturnCreatedWhenBodyValid() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(productDTO);

        mockMvc.perform(post("/products")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(productDTO.getName()))
                .andExpect(jsonPath("$.imgUrl").value(productDTO.getImgUrl()))
                .andExpect(jsonPath("$.price").value(productDTO.getPrice()));

    }

}