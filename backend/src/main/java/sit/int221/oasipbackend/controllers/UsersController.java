package sit.int221.oasipbackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.oasipbackend.dtos.SimpleEventCategoryDTO;
import sit.int221.oasipbackend.dtos.UserCreateDTO;
import sit.int221.oasipbackend.dtos.UserDTO;
import sit.int221.oasipbackend.dtos.UserUpdateDTO;
import sit.int221.oasipbackend.entities.User;
import sit.int221.oasipbackend.repositories.UsersRepository;
import sit.int221.oasipbackend.services.UserService;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/users")
public class UsersController {
    @Autowired
    private UserService userService;

    @Autowired
    private UsersRepository usersRepository;

    @GetMapping("")
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable Integer id){return userService.getUserById(id);}

    @PostMapping("")
    public Object addUser(@RequestBody @Valid UserCreateDTO newUser){
        return userService.create(newUser);
    }

//    @DeleteMapping("/{id}")
//    public void delete(@PathVariable Integer id) {
//        userService.delete(id);
//    }
@DeleteMapping("/{id}")
public void delete(@PathVariable Integer id ) {
    usersRepository.findById(id).orElseThrow(()->
            new ResponseStatusException(HttpStatus.NOT_FOUND,
                    id + " does not exist !!!"));
    usersRepository.deleteById(id);
}

    @PutMapping("/{id}")
    public Object updateUser(@Valid @RequestBody UserUpdateDTO updateUser, @PathVariable Integer id) {
        return userService.updateUser(updateUser, id);
    }
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.OK)
    public Object createUser(@Valid @RequestBody UserCreateDTO newUser) {
        return userService.create(newUser);
    }
}
