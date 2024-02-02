package danieltsuzuki.com.github.catalogo.dto;

import danieltsuzuki.com.github.catalogo.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO implements Serializable {

    private Long id;

    private String authority;


    public RoleDTO(Role entity){
        this.id = entity.getId();
        this.authority = entity.getAuthority();
    }

}
