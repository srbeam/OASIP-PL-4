package sit.int221.oasipbackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sit.int221.oasipbackend.dtos.*;
import sit.int221.oasipbackend.entities.Event;
import sit.int221.oasipbackend.services.EventService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@CrossOrigin
public class EventController {
    @Autowired
    private EventService service;

    @GetMapping("")
    public List<SimpleEventDTO> getAllEvent() {
        return service.getAllEvent();
    }

    @GetMapping("/page")
    public EventPageDTO getAllEventPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int pageSize,
            @RequestParam(defaultValue = "eventStartTime") String sortBy) {
        return service.getAllEventPage(page,pageSize,sortBy);
    }

    @GetMapping("/{id}")
    public EventDetailDTO getEventById(@PathVariable Integer id) {
        return service.getEventById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public Object addNewEvent(@Valid @RequestBody EventCreateDTO newEvent) {
        return service.save(newEvent);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }

    @PutMapping("/reschedule/{id}")
    public Object reschedule(@Valid @RequestBody EventRescheduleDTO updateData, @PathVariable Integer id) {
        return service.reschedule(updateData, id);
    }

    @PutMapping("/{id}")
    public Event update(@RequestBody Event updateEvent, @PathVariable Integer id) {
        return service.update(updateEvent, id);
    }

}
