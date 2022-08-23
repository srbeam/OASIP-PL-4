package sit.int221.oasipbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.int221.oasipbackend.entities.Event;

public interface EventRepository extends JpaRepository<Event, Integer> {
//    List<Event> findAll(Sort.Direction desc, Sort eventStartTime);
}