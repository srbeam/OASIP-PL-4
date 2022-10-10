package sit.int221.oasipbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.int221.oasipbackend.entities.EventCategoryOwner;

public interface EventCategoryOwnerRepository extends JpaRepository<EventCategoryOwner, Integer> {

}