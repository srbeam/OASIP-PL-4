package sit.int221.oasipbackend.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sit.int221.oasipbackend.entities.EventCategory;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventRescheduleDTO {
    @JsonFormat(pattern="dd/MM/yyyy, HH:mm:ss")
    @NotNull(message = "Start time must not be null")
    @Future(message = "Start time must be a future date")
    private LocalDateTime eventStartTime;

    @Size(max = 500, message = "Note size must between 0 to 500")
    private String eventNote;

    @Min(1)
    @Max(480)
    @NotNull(message = "Duration must not be blank")
    private Integer eventDuration;

    @NotNull(message = "Category must not be null")
    private EventCategory EventCategory;
}
