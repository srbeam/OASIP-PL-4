package sit.int221.oasipbackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sit.int221.oasipbackend.dtos.*;
import sit.int221.oasipbackend.entities.Event;
import sit.int221.oasipbackend.repositories.EventRepository;
import sit.int221.oasipbackend.services.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@CrossOrigin("*")
public class EventController {
    @Autowired
    private EventService eventService;

    @GetMapping("")
    public List<SimpleEventDTO> getAllEvent(HttpServletRequest httpServletRequest){
        return eventService.getAll(httpServletRequest);
    }

    @GetMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public EventDetailDTO getEventById(@PathVariable Integer id, HttpServletRequest request) {
        return eventService.getEventById(id, request);
    }


    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping("")
//    @PreAuthorize("!isAuthenticated() or hasAnyRole(\"admin\",\"student\")")
    public void addEvent(HttpServletRequest request , @Valid @RequestBody Event event) {
        eventService.save(request,event);
    }

//    @DeleteMapping("/{id}")
//    public void delete(@PathVariable Integer id) {
//        eventService.delete(id);
//    }

    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable Integer id,HttpServletRequest request) { eventService.delete(id,request);}

//    @PutMapping("/{id}")
//    @ResponseStatus(code = HttpStatus.OK)
//    public Object reschedule(@Valid @RequestBody EventRescheduleDTO updateData, @PathVariable Integer id) {
//        return eventService.reschedule(updateData, id);
//    }
    @PutMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public Object reschedule(@Valid HttpServletRequest request,@Valid @RequestBody EventRescheduleDTO updateData, @PathVariable Integer id) {
        return eventService.reschedule(request,updateData, id);
    }


}
