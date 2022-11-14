package sit.int221.oasipbackend.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter @Setter
@Entity
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "eventId", nullable = false)
    private Integer id;

    @NotBlank(message = "Booking Name is not empty")
    @Size(max = 100,message = "Booking Name size must between 0 and 100 character")
    @Column(name = "bookingName", nullable = false, length = 100)
    private String bookingName;

    @Email(message = "Email must be a well-formed email address" ,regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
    @NotBlank(message = "Email cannot be empty")
    @Column(name = "bookingEmail", nullable = false, length = 100)
    private String bookingEmail;


    @NotNull(message = "StartTime cannot null")
    @Future(message = "StartTime must be a future date")
    @JsonFormat(pattern="dd/MM/yyyy, HH:mm:ss")
    @Column(name = "eventStartTime", nullable = false)
    private LocalDateTime eventStartTime;


    @Column(name = "eventDuration", nullable = false)
    private Integer eventDuration;

    @Size(max = 500,message = "Notes size must between 0 and 500 character")
    @Column(name = "eventNote", length = 500)
    private String eventNote;

    @Column(name = "fileName", length = 500)
    private  String fileName;

    @ManyToOne(optional = false)
    @JoinColumn(name = "eventCategoryId", nullable = false)
    private EventCategory eventCategory;

    public String eventCategoryName(){
        return this.eventCategory.getEventCategoryName();
    }
}