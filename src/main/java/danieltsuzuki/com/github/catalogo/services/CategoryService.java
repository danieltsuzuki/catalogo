package danieltsuzuki.com.github.catalogo.services;

import danieltsuzuki.com.github.catalogo.dto.CategoryDTO;
import danieltsuzuki.com.github.catalogo.entities.Category;
import danieltsuzuki.com.github.catalogo.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll(){
        return repository.findAll().stream().map(CategoryDTO::new).collect(Collectors.toList());
    }

}
