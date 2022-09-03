package sit.int221.oasipbackend;

import org.apache.tomcat.jni.Local;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.util.Date;

@SpringBootApplication
public class OasipBackEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(OasipBackEndApplication.class, args);
    }


}
