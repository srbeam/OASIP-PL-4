package sit.int221.oasipbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sit.int221.oasipbackend.entities.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {
    List<Event> findAllByOrderByEventStartTimeDesc();
    List<Event> findByBookingEmail(String email);
    @Query(value = "SELECT e1 FROM Event e1 JOIN EventCategoryOwner e2 ON e1.eventCategory.id = e2.eventCategory.id " +
            "JOIN User u ON u.id = e2.user.id WHERE u.email = :email")
    List<Event> findEventCategoryOwnerByEmail(@Param("email") String email);

}