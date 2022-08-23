package sit.int221.oasipbackend.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import sit.int221.oasipbackend.Role;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;


@Getter @Setter
public class UserDTO {
    private Integer id;
    private String name;
    private String email;
    private Role role = Role.student;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedOn;
}
