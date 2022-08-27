package sit.int221.oasipbackend.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import sit.int221.oasipbackend.Role;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.time.LocalDateTime;


@Getter @Setter
public class UserUpdateDTO {
    @NotBlank(message = "name must not be blank")
    @Size(max = 100, message = "name size must not be more than 100")
    private String name;
    @NotBlank(message = "email must not be blank")
    @Size(max = 50, message = " email size must not be more than 50")
    @Email(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$",
            message = "Email is invalid")
    private String email;

//    @NotBlank(message = "password must not be blank")
//    @Size(max = 14, message = "password size must not be more than 14")
//    @Size(min = 8, message = "password size must be at least 8")
//    private String password;

    private Role role;

}
