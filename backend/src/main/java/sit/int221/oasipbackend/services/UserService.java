package sit.int221.oasipbackend.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.oasipbackend.Role;
import sit.int221.oasipbackend.config.JwtTokenUtil;
import sit.int221.oasipbackend.dtos.UserCreateDTO;
import sit.int221.oasipbackend.dtos.UserDTO;
import sit.int221.oasipbackend.dtos.UserLoginDTO;
import sit.int221.oasipbackend.dtos.UserUpdateDTO;
import sit.int221.oasipbackend.entities.User;
import sit.int221.oasipbackend.exceptions.ValidationHandler;
import sit.int221.oasipbackend.model.JwtResponse;
import sit.int221.oasipbackend.repositories.UsersRepository;
import sit.int221.oasipbackend.utils.ListMapper;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ListMapper listMapper;
    @Autowired
    private Argon2PasswordEncoder argon2;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private JwtUserDetailsService userDetailsService;


    public List<UserDTO> getAllUsers() {
        Sort sort = Sort.by("name");
        List<User> userList = usersRepository.findAll(sort.ascending());
        return listMapper.mapList(userList, UserDTO.class, modelMapper);
    }

    public UserDTO getUserById(Integer id) {
        User user = usersRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User id not found"));
        return modelMapper.map(user , UserDTO.class);
    }
    private ArrayList<String> userNameAll = new ArrayList<String>();


    public Boolean validateDuplicateName(Integer id,String name) {
        List<User> userList = usersRepository.findAll();
        for (User user : userList) {
            if (user.getId().intValue() != id.intValue()) {
                userNameAll.add(user.getName());
            }
        }
        return !userNameAll.contains(name);

    }
    public Boolean validateDuplicateEmail(Integer id,String email) {
        List<User> userList = usersRepository.findAll();
        for (User user : userList) {
            if (user.getId().intValue() != id.intValue()) {
                userNameAll.add(user.getEmail());
            }
        }
        return !userNameAll.contains(email);

    }



    public Object create(UserCreateDTO newUser) {
        if(!validateDuplicateName(newUser.getId(),newUser.getName())) {
            return ValidationHandler.showError("name", "name must be unique");

        }else if(!validateDuplicateEmail(newUser.getId(),newUser.getEmail())){
            return ValidationHandler.showError("name", "email must be unique");
        }

        else {
            User user = modelMapper.map(newUser, User.class);
            user.setName(newUser.getName().trim());
            user.setEmail(newUser.getEmail().trim());
            user.setPassword(argon2.encode(newUser.getPassword()));
            user.setRole(newUser.getRole()==null? Role.student : newUser.getRole());
            return usersRepository.saveAndFlush(user);

        }
    }


    public void delete(@PathVariable Integer id) {
        usersRepository.findById(id).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        id + " does not exist !!!"));
        usersRepository.deleteById(id);
    }


    public Object updateUser(UserUpdateDTO updateUser, Integer id){
        User user = usersRepository.findById(id).orElseThrow(()->
            new ResponseStatusException(HttpStatus.NOT_FOUND, id + "does not exist!!!"));
    if(checkUnique(updateUser , id)){
        user.setName(updateUser.getName().trim());
        user.setEmail(updateUser.getEmail().trim());
//        user.setPassword(updateUser.getPassword());
        user.setRole((updateUser.getRole() == null) ? user.getRole() : updateUser.getRole());
        usersRepository.saveAndFlush(user);
    }
    return updateUser;
}

    public boolean checkUnique (UserUpdateDTO user ,Integer id){
        List<User> allUser = usersRepository.findAll();
        for(User users : allUser){
            if(!(users.getId() == id)){
                if(users.getName().trim().equals(user.getName().trim())){
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "This name is already used");
                }if (users.getEmail().trim().equals(user.getEmail().trim())){
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "This email is already used");
                }
            }
        }
        return true;
    }

//    public UserLoginDTO signIn(UserLoginDTO userSignIn) {
//        if (usersRepository.existsByEmail(userSignIn.getEmail())) {
//            User user = usersRepository.findByEmail(userSignIn.getEmail());
//            if (argon2.matches(userSignIn.getPassword(), user.getPassword())) {
//                throw new ResponseStatusException(HttpStatus.OK, "Password Matched");
//            } else {
//                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Password NOT Matched");
//            }
//        } else {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "A user with the specified email DOES NOT exist");
//        }
//    }


    public ResponseEntity loginDTO(UserLoginDTO userLogin) throws  Exception {
        if (usersRepository.existsByEmail(userLogin.getEmail())) {
            User user = usersRepository.findByEmail(userLogin.getEmail());
            if (argon2.matches(userLogin.getPassword(), user.getPassword())) {
                authenticate(userLogin.getEmail() , userLogin.getPassword());
//                authenticate(userLogin.getEmail(), userLogin.getPassword());

                final UserDetails userDetails = userDetailsService
                        .loadUserByUsername(userLogin.getEmail());

                final String token = jwtTokenUtil.generateToken(userDetails);

//                return ResponseEntity.ok(new JwtResponse(token));
                return ResponseEntity.ok(new JwtResponse(token));
//                throw new ResponseStatusException(HttpStatus.OK, "Password Matched");
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Password NOT Matched");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "A user with the specified email DOES NOT exist");
        }
    }

    private void authenticate(String email, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}

