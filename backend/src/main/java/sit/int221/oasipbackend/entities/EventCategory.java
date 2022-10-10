package sit.int221.oasipbackend.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;

@Getter @Setter
@Entity
@Table(name = "eventCategory")
public class EventCategory {

    @Id
    @Column(name = "eventCategoryId", nullable = false)
    private Integer id;

    @NotBlank(message = "Category name must not be blank")
    @Size(max = 100, message = "Category name size must not be more than 100")
    @Column(name = "eventCategoryName", nullable = false, length = 100)
    private String eventCategoryName;

    @Size(max = 500, message = "Category description size must not be more than 500")
    @Column(name = "eventCategoryDescription", length = 500)
    private String eventCategoryDescription;

    @Min(1)
    @Max(480)
    @NotNull(message = "Category duration must not be blank")
    @Column(name = "eventDuration", nullable = false)
    private Integer eventDuration;

    public void setEventCategoryName(String eventCategoryName) {
        this.eventCategoryName = eventCategoryName;
    }
    public String getEventCategoryNames() {
        return eventCategoryName;
    }
}