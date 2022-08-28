package sit.int221.oasipbackend.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import sit.int221.oasipbackend.Role;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Entity
@Getter @Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 100 , unique = true)
    private String name;

    @Column(name = "email", nullable = false, length = 50 , unique = true)
    private String email;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role = Role.student;


    @Column(name = "createdOn", nullable = false , insertable = false,updatable = false)
    private LocalDateTime createdOn;

    @Column(name = "updatedOn", nullable = false, insertable = false,updatable = false)
    private LocalDateTime updatedOn;


}