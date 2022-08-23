package sit.int221.oasipbackend.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import sit.int221.oasipbackend.entities.EventCategory;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

@Getter
@Setter
public class EventDetailDTO {
    private Integer id;
    private String bookingName;
    private String bookingEmail;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventStartTime;
    private Integer eventDuration;
    private String eventNote;
    private Integer eventCategoryId;
    private EventCategory eventCategory;

}
