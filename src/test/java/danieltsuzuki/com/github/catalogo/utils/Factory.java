package danieltsuzuki.com.github.catalogo.utils;

import danieltsuzuki.com.github.catalogo.dto.CategoryDTO;
import danieltsuzuki.com.github.catalogo.dto.ProductDTO;
import danieltsuzuki.com.github.catalogo.entities.Category;
import danieltsuzuki.com.github.catalogo.entities.Product;

import java.math.BigDecimal;
import java.time.Instant;

public class Factory {

    public static Product createProduct() {
        Product product =  new Product(
                1l, "Iphone", "Good phone", new BigDecimal(800.0),
                "", Instant.now()
        );
        product.getCategories().add(new Category(2l, "Electronics"));
        return product;
    }

    public static ProductDTO createProductDTO() {
        Product product = createProduct();
        return new ProductDTO(product, product.getCategories());
    }

    public static Category createCategory() {
        return new Category(1l, "electronic");
    }

    public static CategoryDTO createCategoryDTO() {
        return new CategoryDTO(createCategory());
    }

}
