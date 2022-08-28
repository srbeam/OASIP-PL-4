package sit.int221.oasipbackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sit.int221.oasipbackend.dtos.UserSignInDTO;
import sit.int221.oasipbackend.services.UserService;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/api/match")
public class UserSignInController {
    @Autowired
    private UserService userService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public UserSignInDTO signIn(@Valid @RequestBody UserSignInDTO userSignIn) {
        return userService.signIn(userSignIn);
    }
}
