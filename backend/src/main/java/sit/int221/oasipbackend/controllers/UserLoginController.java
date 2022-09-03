package sit.int221.oasipbackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sit.int221.oasipbackend.dtos.UserLoginDTO;
import sit.int221.oasipbackend.entities.User;
import sit.int221.oasipbackend.services.UserService;

import javax.validation.Valid;
@RestController
@RequestMapping("/api/login")
//@RequestMapping("/api/match")
public class UserLoginController {

    @Autowired
    private UserService userService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity loginDTO
            (@Valid @RequestBody UserLoginDTO userLogin) throws Exception {
        return userService.loginDTO(userLogin);
    }
}
