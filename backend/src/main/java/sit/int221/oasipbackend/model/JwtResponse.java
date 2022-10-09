package sit.int221.oasipbackend.model;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class JwtResponse {
    private String message;
    private String accessToken;
    private String refreshToken;

}