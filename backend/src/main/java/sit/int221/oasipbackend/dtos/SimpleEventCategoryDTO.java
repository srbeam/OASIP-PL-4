package sit.int221.oasipbackend.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
public class SimpleEventCategoryDTO {

    @NotBlank(message = "Category name must not be blank")
    @Size(max = 100, message = "Category name size must not be more than 100")
    private String eventCategoryName;

    @Size(max = 500, message = "Category description size must not be more than 500")
    private String eventCategoryDescription;

    @Min(1)
    @Max(480)
    @NotNull(message = "Category duration must not be blank")
    private Integer eventDuration;

    public String getCategoryName() {
        return eventCategoryName;
    }
}
