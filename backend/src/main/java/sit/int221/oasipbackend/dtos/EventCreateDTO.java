package sit.int221.oasipbackend.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sit.int221.oasipbackend.entities.EventCategory;

import javax.validation.constraints.*;
import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventCreateDTO {
    @NotBlank(message = "Booking name must not be blank")
    @Size(max = 100, message = "Booking name size must not be more than 100")
    private String bookingName;

    @NotBlank(message = "Booking email must not be blank")
    @Size(max = 100, message = "Booking email size must not be more than 100")
    @Email(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$",
           message = "Email is invalid")
    private String bookingEmail;

    @NotNull(message = "Start time must not be null")
    @Future(message = "Start time must be a future date")
    @JsonFormat(pattern="dd/MM/yyyy, HH:mm:ss")
    private LocalDateTime eventStartTime;

    @Min(1)
    @Max(480)
    @NotNull(message = "Duration must not be blank")
    private Integer eventDuration;

    @NotNull(message = "Category must not be null")
    private EventCategory eventCategory;

    @Size(max = 500, message = "Note size must between 0 to 500")
    private String eventNote;

}
