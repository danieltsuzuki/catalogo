package danieltsuzuki.com.github.catalogo.services.validation;

import danieltsuzuki.com.github.catalogo.dto.UserUpdateDTO;
import danieltsuzuki.com.github.catalogo.entities.User;
import danieltsuzuki.com.github.catalogo.repositories.UserRepository;
import danieltsuzuki.com.github.catalogo.resources.exceptions.FieldMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserUpdateValidator implements ConstraintValidator<UserUpdateValid, UserUpdateDTO> {

    @Autowired
    private UserRepository repository;

    @Autowired
    private HttpServletRequest request;

    @Override
    public void initialize(UserUpdateValid constraintAnnotation) {
    }

    @Override
    public boolean isValid(UserUpdateDTO dto, ConstraintValidatorContext context) {
        List<FieldMessage> list = new ArrayList<>();

        @SuppressWarnings("unchecked")
        var uriVars = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        Long userId = Long.parseLong(uriVars.get("id"));

        User user = repository.findByEmail(dto.getEmail());

        if (user != null && userId != user.getId()) {
            list.add(new FieldMessage("email", "Email already exists"));
        }

        for (FieldMessage e: list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage())
                    .addPropertyNode(e.getFieldName())
                    .addConstraintViolation();
        }

        return list.isEmpty();
    }
}
