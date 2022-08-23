package sit.int221.oasipbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.int221.oasipbackend.entities.EventCategory;

public interface EventCategoryRepository extends JpaRepository<EventCategory, Integer> {
}