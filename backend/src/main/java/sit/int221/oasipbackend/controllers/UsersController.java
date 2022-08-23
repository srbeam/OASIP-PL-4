package sit.int221.oasipbackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sit.int221.oasipbackend.dtos.SimpleEventCategoryDTO;
import sit.int221.oasipbackend.dtos.UserCreateDTO;
import sit.int221.oasipbackend.dtos.UserDTO;
import sit.int221.oasipbackend.dtos.UserUpdateDTO;
import sit.int221.oasipbackend.entities.User;
import sit.int221.oasipbackend.services.UserService;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/users")
public class UsersController {
    @Autowired
    private UserService userService;

    @GetMapping("")
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable Integer id){return userService.getUserById(id);}

    @PostMapping("")
    public Object addEvent(@RequestBody @Valid UserCreateDTO newUser){
        return userService.create(newUser);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        userService.delete(id);
    }

    @PutMapping("/{id}")
    public Object updateUser(@Valid @RequestBody UserUpdateDTO updateUser, @PathVariable Integer id) {
        return userService.updateUser(updateUser, id);
    }
}
