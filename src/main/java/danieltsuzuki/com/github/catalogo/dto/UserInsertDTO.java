package danieltsuzuki.com.github.catalogo.dto;

import danieltsuzuki.com.github.catalogo.services.validation.UserInsertValid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@UserInsertValid
public class UserInsertDTO extends  UserDTO {

    private String password;
}
