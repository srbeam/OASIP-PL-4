package sit.int221.oasipbackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.oasipbackend.dtos.*;
import sit.int221.oasipbackend.entities.Event;
import sit.int221.oasipbackend.entities.EventCategory;
import sit.int221.oasipbackend.repositories.EventCategoryRepository;
import sit.int221.oasipbackend.repositories.EventRepository;
import sit.int221.oasipbackend.services.EmailSenderService;
import sit.int221.oasipbackend.services.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@CrossOrigin("*")
public class EventController {
    @Autowired
    private EventService eventService;

    @Autowired
    private EventCategoryRepository eventCategoryRepository;

    @Autowired
    private EmailSenderService emailSenderService;

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
    public void addEvent (@Valid HttpServletRequest request ,
                          @Valid @RequestPart("event") EventCreateDTO eventCreateDTO ,
                          @RequestPart(value = "file" , required = false) MultipartFile multipartFile) {
        eventService.save(request,eventCreateDTO,multipartFile);

    }

    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable Integer id,HttpServletRequest request) { eventService.delete(id,request);}


//    @PutMapping("/{id}")
//    @ResponseStatus(code = HttpStatus.OK)
//    public Object reschedule(@Valid HttpServletRequest request,@Valid @RequestBody EventRescheduleDTO updateData, @PathVariable Integer id) {
//        return eventService.reschedule(request,updateData, id);
//    }
    @PutMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public Object reschedule(HttpServletRequest request,@Valid @RequestPart("event")  EventRescheduleDTO updateData, @PathVariable Integer id,@RequestPart(value = "file" , required = false) MultipartFile multipartFile) throws IOException {
        return eventService.reschedule(request,updateData, id,multipartFile);
    }


}
