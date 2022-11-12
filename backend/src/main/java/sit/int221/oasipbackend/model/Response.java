package sit.int221.oasipbackend.model;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
@ControllerAdvice
public class Response {
    private HttpStatus httpStatus;
    private String message;

    public static ResponseEntity<Object> response(HttpStatus httpStatus, String message){
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("timestamp", new Date());
        map.put("status", httpStatus.value());
        map.put("error", httpStatus.toString().replaceAll("\\d","").trim());
        map.put("message", message);
        return new ResponseEntity<Object>(map,httpStatus);
    }
    //    Max file
    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFiles;

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity handleMultipartException(MaxUploadSizeExceededException maxUploadSizeExceededException){
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("timestamp",new Date());
        map.put("status",HttpStatus.BAD_REQUEST.value());
        map.put("error",HttpStatus.PAYLOAD_TOO_LARGE.name());
        map.put("message","The file size cannot be larger than 10 MB.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
    }
}
