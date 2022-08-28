package sit.int221.oasipbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sit.int221.oasipbackend.entities.User;


public interface UsersRepository extends JpaRepository<User, Integer> {
    public User findByEmail(String Email);
    public boolean existsByEmail(String Email);
}