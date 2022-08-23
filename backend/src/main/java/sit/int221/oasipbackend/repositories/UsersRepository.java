package sit.int221.oasipbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.int221.oasipbackend.entities.User;

public interface UsersRepository extends JpaRepository<User, Integer> {
}