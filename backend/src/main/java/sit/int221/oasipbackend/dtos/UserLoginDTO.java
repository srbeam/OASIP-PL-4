package sit.int221.oasipbackend.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;

@Getter
@Setter
public class UserLoginDTO {
//    private String name;
@Email(message = "invalid email format")
private String email;
    private String password;


}
