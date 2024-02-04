package danieltsuzuki.com.github.catalogo.services;

import danieltsuzuki.com.github.catalogo.dto.RoleDTO;
import danieltsuzuki.com.github.catalogo.dto.UserDTO;
import danieltsuzuki.com.github.catalogo.dto.UserInsertDTO;
import danieltsuzuki.com.github.catalogo.dto.UserUpdateDTO;
import danieltsuzuki.com.github.catalogo.entities.Role;
import danieltsuzuki.com.github.catalogo.entities.User;
import danieltsuzuki.com.github.catalogo.repositories.RoleRepository;
import danieltsuzuki.com.github.catalogo.repositories.UserRepository;
import danieltsuzuki.com.github.catalogo.services.exceptions.DatabaseException;
import danieltsuzuki.com.github.catalogo.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Page<UserDTO> findAllPaged(Pageable pageable){
        Page<User> list = repository.findAll(pageable);
        return list.map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id){
        User entity = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Entity not found")
        );
        return new UserDTO(entity);
    }

    @Transactional
    public UserDTO insert(UserInsertDTO dto) {
        User entity = new User();
        copyDtoToEntity(dto, entity);
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        entity = repository.save(entity);
        return new UserDTO(entity);
    }

    @Transactional
    public UserDTO update(Long id, UserUpdateDTO dto) {
        try{
            User entity = repository.getReferenceById(id);
            copyDtoToEntity(dto, entity);
            entity = repository.save(entity);
            return new UserDTO(entity);
        } catch (jakarta.persistence.EntityNotFoundException e) {
            throw new ResourceNotFoundException("ID not found " + id);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if(!repository.existsById(id)){
            throw new ResourceNotFoundException("ID not found " + id);
        }
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity violation");
        }
    }

    private void copyDtoToEntity(UserDTO dto, User entity){
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());

        entity.getRoles().clear();
        for(RoleDTO roleDTO : dto.getRoles()){
            Role role = roleRepository.getReferenceById(roleDTO.getId());
            entity.getRoles().add(role);
        }
    }

}
