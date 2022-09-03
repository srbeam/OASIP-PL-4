package sit.int221.oasipbackend.services;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sit.int221.oasipbackend.entities.User;
import sit.int221.oasipbackend.repositories.UsersRepository;

import java.util.ArrayList;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UsersRepository usersRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = usersRepository.findByEmail(email);
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword() , new ArrayList<>());
    }
}