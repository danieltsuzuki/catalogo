package danieltsuzuki.com.github.catalogo.dto;

import danieltsuzuki.com.github.catalogo.services.validation.UserInsertValid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @NotBlank
    @Size(min = 8, message = "Must have at least 8 characters")
    private String password;
}
