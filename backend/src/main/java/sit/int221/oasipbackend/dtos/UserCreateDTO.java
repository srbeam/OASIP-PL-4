package sit.int221.oasipbackend.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;
import sit.int221.oasipbackend.Role;
import sit.int221.oasipbackend.entities.EventCategory;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.*;
import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDTO {
    private  int id;

    @NotBlank(message = "name must not be blank")
    @Size(max = 100, message = "name size must not be more than 100")
    private String name;

    @NotBlank(message = "email must not be blank")
    @Size(max = 50, message = " email size must not be more than 50")
    @Email(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$",
           message = "Email is invalid")
    private String email;

//    @NotNull(message = "Start time must not be null")
//    @Future(message = "Start time must be a future date")
//    @JsonFormat(pattern="dd/MM/yyyy, HH:mm:ss")
//    @NotBlank(message = "role must not be blank")
    @Enumerated()
    private Role role;





}
