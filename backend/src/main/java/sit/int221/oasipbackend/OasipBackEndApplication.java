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
//        LocalDateTime ldt = LocalDateTime.now();
//        LocalDateTime n = ldt.withSecond(0).withNano(0);
//        System.out.println(ldt.isEqual(n));
//        System.out.println(ldt);
//        System.out.println(ldt.withSecond(0).withNano(0));

//        Integer duration = 30;
//        LocalDateTime startDTOld = LocalDateTime.of(2022,05,20,10,00);
//        LocalDateTime startDTNew = LocalDateTime.of(2022,05,20,10,20);
//
//        System.out.println(checkOverLab(startDTNew, startDTOld, duration));
//        System.out.println(132 != 132);


    }

    private static Boolean checkOverLab(LocalDateTime startDTNew, LocalDateTime startDTOld, Integer duration) {
        LocalDateTime endDTOld = startDTOld.plusMinutes(duration);
        LocalDateTime startDanger = startDTOld.minusMinutes(duration);

        if(startDTNew.isAfter(endDTOld) && !startDTNew.isEqual(endDTOld)) {
            System.out.println("true ไม่ซ้อน อันเเรก");
//                status = true;
            return true;
        }else {
            if(startDTNew.isBefore(startDanger) && !startDTNew.isEqual(startDanger)) {
                System.out.println("true ไม่ซ้อน อันสอง");
//                    status = true;
                return true;
            }
//                else {
//                    System.out.println("false ตรง");
//                    status = false;
//                    return false;
//                }
        }
        System.out.println("false ซ้อน");
        return false;

    }


}
