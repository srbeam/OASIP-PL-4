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
//    public void addEvent(HttpServletRequest request , @Valid @RequestBody Event event,@RequestPart(value = "file", required = false) MultipartFile multipartFile) {
//
//        int categoryId = event.getEventCategory().getId();
//        EventCategory eventCategory = eventCategoryRepository.findById(categoryId).orElseThrow(()->new ResponseStatusException(
//                HttpStatus.NOT_FOUND, "Category id "+ categoryId+
//                "Does Not Exist !!!"
//        ));
//        LocalDateTime time = event.getEventStartTime();
//        String formattedDate = time.format(DateTimeFormatter.ofPattern("E MMM dd, yyyy HH:mm").withZone(ZoneId.of("UTC")));
//        String header = "You have made a new appointment ." + '\n';
////        String body = "Your appointment has been registered successfully. \n \n" +
////                "Details  \n" + "Name : " + event.getBookingName() + "\n" +"Clinic : " + eventCategory.getEventCategoryName() +
////                "\n" + "Date : " + formattedDate + "\n" + "Note : " + event.getEventNote();
//
//        String body =
//                "Subject : [OASIP]" + " " + event.getEventCategory().getEventCategoryName() +
//                        " " + "@" + " " + formattedDate + " " + "-" + " " +
//                        findEndDate(event.getEventStartTime(),event.getEventDuration()).toString().substring(11) +
//                        " (ICT)" + '\n' +
//                        "Reply-to : " + "reply@intproj21.sit.kmutt.ac.th" + '\n' +
//                        "Booking Name : " + event.getBookingName() + '\n' +
//                        "Event Category : " + event.getEventCategory().getEventCategoryName() + '\n' +
//                        "When : " + formattedDate + " " + "-" + " " +
//                        findEndDate(event.getEventStartTime(),event.getEventDuration()).toString().substring(11) + " (ICT)" + '\n' +
////                          "When : " + " " + e.getEventStartTime().toString().replace("T" , " ")+ " " + "-" + " " + findEndDate(e.getEventStartTime(),e.getEventDuration()).toString().substring(11) + '\n' +
////                          "Event duration : " + e.getEventDuration() + "Minutes" + '\n' +
//                        "Event note : "  + event.getEventNote();
//
//        eventService.save(request,event,multipartFile);
//
//        emailSenderService.sendEmail(event.getBookingEmail(),header,body);
//    }

    public LocalDateTime findEndDate(LocalDateTime date, Integer duration){
        return date.plusMinutes(duration);
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
