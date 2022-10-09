package sit.int221.oasipbackend.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
public class SimpleEventCategoryDTO {
    private String eventCategoryName;
    private String eventCategoryDescription;
    private Integer eventDuration;
    public String getCategoryName() {
        return eventCategoryName;
    }
}
