package sit.int221.oasipbackend.model;

import org.springframework.http.HttpStatus;

public class HandleException extends RuntimeException{
    private HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

    public HandleException(HttpStatus httpStatus , String message){
        super(message);
        this.httpStatus = httpStatus;
    }
    public HttpStatus getHttpStatus(){
        return httpStatus;
    }
}