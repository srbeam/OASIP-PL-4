package sit.int221.oasipbackend.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.oasipbackend.Role;
import sit.int221.oasipbackend.dtos.UserCreateDTO;
import sit.int221.oasipbackend.dtos.UserDTO;
import sit.int221.oasipbackend.dtos.UserUpdateDTO;
import sit.int221.oasipbackend.entities.User;
import sit.int221.oasipbackend.exceptions.ValidationHandler;
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
            user.setPassword(newUser.getPassword());
            user.setRole(newUser.getRole()==null? Role.student : newUser.getRole());
            return usersRepository.saveAndFlush(user);

        }
    }


//    public UserDTO save(@Valid CreateUserDTO user) {
//        User newUser = modelMapper.map(user, User.class);
//        newUser.setName(user.getName().trim());
//        newUser.setEmail(user.getEmail().trim());
//        userRepository.saveAndFlush(newUser);
//        return modelMapper.map(newUser, UserDTO.class);}

    public void delete(@PathVariable Integer id) {
        usersRepository.findById(id).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        id + " does not exist !!!"));
        usersRepository.deleteById(id);
    }

//    public Object updateUser(UserUpdateDTO updateUser, Integer id) {
//        User existingUser = usersRepository.findById(id).orElseThrow(
//                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, id + " Dose not exits!!!"));
//        if(!validateDuplicateName(id,updateUser.getName())){
//            return ValidationHandler.showError("name", "name is NOT unique");
//        }else if(!validateDuplicateEmail(id, updateUser.getEmail())){
//            return ValidationHandler.showError("email", "email is NOT unique");
//        }
////        else if(updateUser.getRole() == null) {
////            updateUser.setRole(existingUser.getRole());
////        }else if(existingUser.getRole().equals(updateUser.getRole())){
////            existingUser.setRole(updateUser.getRole());
////        }
//        else {
//            existingUser.setName(updateUser.getName().trim());
//            existingUser.setEmail(updateUser.getEmail().trim());
//            existingUser.setRole((updateUser.getRole() == null)? existingUser.getRole(): (updateUser.getRole()));
//            usersRepository.saveAndFlush(existingUser);
//        }
//        return existingUser;
//    }
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


}
