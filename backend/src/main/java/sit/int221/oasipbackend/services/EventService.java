package sit.int221.oasipbackend.services;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.oasipbackend.config.JwtTokenUtil;
import sit.int221.oasipbackend.dtos.*;
import sit.int221.oasipbackend.entities.Event;
import sit.int221.oasipbackend.entities.EventCategory;
import sit.int221.oasipbackend.entities.EventCategoryOwner;
import sit.int221.oasipbackend.entities.User;
import sit.int221.oasipbackend.exceptions.ValidationHandler;
import sit.int221.oasipbackend.model.HandleException;
import sit.int221.oasipbackend.repositories.EventCategoryOwnerRepository;
import sit.int221.oasipbackend.repositories.EventCategoryRepository;
import sit.int221.oasipbackend.repositories.EventRepository;
import sit.int221.oasipbackend.repositories.UsersRepository;
import sit.int221.oasipbackend.utils.ListMapper;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class EventService {
    @Autowired
    private ListMapper listMapper;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private  UsersRepository usersRepository;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private JwtUserDetailsService jwtuserDetailsService;
    @Autowired
    private EventCategoryOwnerRepository eventCategoryOwnerRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private EventService(EventRepository repository) {
        this.eventRepository = repository;
    }

    @Autowired
    private  FileStorageService fileStorageService;

    public Event save( HttpServletRequest request,  EventCreateDTO eventCreateDTO , MultipartFile multipartFile) {
        Event e = modelMapper.map(eventCreateDTO, Event.class);
        if (request.getHeader("Authorization") != null) {
            String getUserEmail = getUserEmail(getRequestAccessToken(request));
            if (request.isUserInRole("student")) {
                if (getUserEmail.equals(eventCreateDTO.getBookingEmail())) {
                        LocalDateTime localDateTime = e.getEventStartTime();
                        String newformat = localDateTime.format(DateTimeFormatter.ofPattern("E MMM dd, yyyy HH:mm").withZone(ZoneId.of("UTC")));
                        String header = "You have made a new appointment ." + '\n';
                        String body =
                                "Subject : [OASIP]" + " " + e.getEventCategory().getEventCategoryName() + " " + "@" + " " + newformat + " " + "-" + " " + findEndDate(e.getEventStartTime(), e.getEventDuration()).toString().substring(11) + " (ICT)" + '\n' +
                                        "Reply-to : " + "noreply@intproj21.sit.kmutt.ac.th" + '\n' +
                                        "Booking Name : " + e.getBookingName() + '\n' +
                                        "Event Category : " + e.getEventCategory().getEventCategoryName() + '\n' +
                                        "When : " + newformat + " " + "-" + " " + findEndDate(e.getEventStartTime(), e.getEventDuration()).toString().substring(11) + " (ICT)" + '\n' +
                                        "Event note : " + e.getEventNote();
                        emailSenderService.sendEmail(e.getBookingEmail(),header,body);
                        System.out.println("email sent succesfully");
                    //            getId send file
                    if (multipartFile != null) {
                        e.setFileName(StringUtils.cleanPath(multipartFile.getOriginalFilename()));
                    }
                    Event saveEvent = eventRepository.saveAndFlush(e);
                    sendFile(multipartFile , saveEvent.getId());
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Booking email must be the same as the student's email");
                }
            }else if (request.isUserInRole("ROLE_lecturer")) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,"No permission to add event");
            }

        }
//      guest
        LocalDateTime localDateTime = e.getEventStartTime();
        String newformat = localDateTime.format(DateTimeFormatter.ofPattern("E MMM dd, yyyy HH:mm").withZone(ZoneId.of("UTC")));
        String header = "You have made a new appointment ." + '\n';
        String body =
                "Subject : [OASIP]" + " " + e.getEventCategory().getEventCategoryName() + " " + "@" + " " + newformat + " " + "-" + " " + findEndDate(e.getEventStartTime(), e.getEventDuration()).toString().substring(11) + " (ICT)" + '\n' +
                        "Reply-to : " + "noreply@intproj21.sit.kmutt.ac.th" + '\n' +
                        "Booking Name : " + e.getBookingName() + '\n' +
                        "Event Category : " + e.getEventCategory().getEventCategoryName() + '\n' +
                        "When : " + newformat + " " + "-" + " " + findEndDate(e.getEventStartTime(), e.getEventDuration()).toString().substring(11) + " (ICT)" + '\n' +
                        "Event note : " + e.getEventNote();
        emailSenderService.sendEmail(e.getBookingEmail(),header,body);
        System.out.println("email sent succesfully");
        if (multipartFile != null) {
            e.setFileName(StringUtils.cleanPath(multipartFile.getOriginalFilename()));
        }
        Event saveEvent = eventRepository.saveAndFlush(e);
        sendFile(multipartFile , saveEvent.getId());
        return eventRepository.saveAndFlush(e);
    }

public void sendFile(MultipartFile multipartFile , Integer id){
    try {
        if (multipartFile != null){
            fileStorageService.storeFile(multipartFile , id);
        }
    }catch (IOException ioException){
        ioException.printStackTrace();
//            System.out.println(e);
    }catch (MaxUploadSizeExceededException maxUploadSizeExceededException){
        throw new HandleException(HttpStatus.BAD_REQUEST,"File Upload");
    }
}




    public Object reschedule(HttpServletRequest request,EventRescheduleDTO updateData, Integer id ,MultipartFile multipartFile) throws IOException {
        Event existingEvent = eventRepository.findById(id).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        id + " does not exist !!!"));
        String getUserEmail = getUserEmail(getRequestAccessToken(request));
        if(request.isUserInRole("ROLE_student")){
            if(getUserEmail.equals(existingEvent.getBookingEmail())){
                if (!validateOverLab(updateData.getEventStartTime(), updateData.getEventCategory(), updateData.getEventDuration(), id)) {
                    return ValidationHandler.showError("eventStartTime", "Have an appointment during this time");
                }else {
                    existingEvent.setEventStartTime(updateData.getEventStartTime());
                    existingEvent.setEventNote(updateData.getEventNote());
//                    return eventRepository.saveAndFlush(existingEvent);
                }
            }else{
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Cannot reschedule event which you didn't own");
            }
        }
        if(request.isUserInRole("ROLE_admin")){
            existingEvent.setEventStartTime(updateData.getEventStartTime());
            existingEvent.setEventNote(updateData.getEventNote());
//            return eventRepository.saveAndFlush(existingEvent);
        }

        //  file
        Path getPath = fileStorageService.getPathFile(id);
        File directoryPath = new File(getPath.toString());
        File[] files = directoryPath.listFiles();

        if (multipartFile == null && updateData.getIsDeleteFile() == false) {
            eventRepository.saveAndFlush(existingEvent);
            return updateData;
        }
//        if (multipartFile == null && directoryPath.isDirectory() == false) {
//            eventRepository.saveAndFlush(existingEvent);
//            return updateData;
//        }

        //ไม่เจอ directory = event นั้นไม่มีไฟล์
        if (directoryPath.isDirectory() == true) {



            if (multipartFile == null) {
                //  check file in folder
                for (File f : files) {
                    if (f.isFile() && f.exists()) {
                        //ลบ directory
                        FileUtils.deleteDirectory(new File(getPath.toString()));
                        existingEvent.setFileName(null);
                    } else {
                        System.out.println("can't delete a file due to open or error");
                    }
                }
            } else {
                directoryPath.isDirectory();
                String fileName = null;
                for (File file1 : files) {
                    if (file1.isFile()) {
                        fileName = file1.getName();
                    }
                }
                if (multipartFile.getOriginalFilename().isEmpty()) {
                    System.out.println("Equal file name");
                } else {
                    System.out.println("No equal file name");
                    if (Files.exists(Path.of(getPath.toString()))) {
                        FileUtils.cleanDirectory(new File(getPath.toString()));
                        existingEvent.setFileName(StringUtils.cleanPath(multipartFile.getOriginalFilename()));
                        fileStorageService.storeFile(multipartFile, id);
                    }
                    fileStorageService.storeFile(multipartFile, id);
                }
            }
        } else {
            Files.createDirectories(directoryPath.toPath());
            fileStorageService.storeFile(multipartFile, id);
            existingEvent.setFileName(StringUtils.cleanPath(multipartFile.getOriginalFilename()));
        }
        return eventRepository.saveAndFlush(existingEvent);
    }
//     public Object reschedule(HttpServletRequest request,EventRescheduleDTO updateData, Integer id ) {
//        Event existingEvent = eventRepository.findById(id).orElseThrow(()->
//                new ResponseStatusException(HttpStatus.NOT_FOUND,
//                        id + " does not exist !!!"));
////        Event e = modelMapper.map(updateData, Event.class);
//        String getUserEmail = getUserEmail(getRequestAccessToken(request));
//        if(request.isUserInRole("ROLE_student")){
//            if(getUserEmail.equals(existingEvent.getBookingEmail())){
//                if (!validateOverLab(updateData.getEventStartTime(), updateData.getEventCategory(), updateData.getEventDuration(), id)) {
//                    return ValidationHandler.showError("eventStartTime", "Have an appointment during this time");
//                }else {
//                    existingEvent.setEventStartTime(updateData.getEventStartTime());
//                    existingEvent.setEventNote(updateData.getEventNote());
//                    return eventRepository.saveAndFlush(existingEvent);
//                }
//            }else{
//                throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Cannot reschedule event which you didn't own");
//            }
//        }
//        if(request.isUserInRole("ROLE_admin")){
//            existingEvent.setEventStartTime(updateData.getEventStartTime());
//            existingEvent.setEventNote(updateData.getEventNote());
//            return eventRepository.saveAndFlush(existingEvent);
//        }
//
//        return eventRepository.saveAndFlush(existingEvent);
//    }
//
    public void delete(Integer id,HttpServletRequest request) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                id + " does not exist !!!"));
        String getUserEmail = getUserEmail(getRequestAccessToken(request));
        User user = usersRepository.findByEmail(getUserEmail);
        if(user != null) {
            if((request.isUserInRole("ROLE_student")) && !event.getBookingEmail().equals(user.getEmail())) {
                System.out.println("Cannot delete event which you didn't own");
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot delete event which you didn't own");
            }
//            if((request.isUserInRole("ROLE_lecturer"))) {
//                System.out.println("Only student, admin can delete event");
//                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only student, admin can delete event");
//            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Please Sign in again");}
        fileStorageService.Deletefile(id);
        eventRepository.deleteById(id);
//        return id;
    }



    public List<SimpleEventDTO> getAll(HttpServletRequest request){
        List<Event> eventsList = eventRepository.findAll(Sort.by(Sort.Direction.DESC,"eventStartTime"));
        String getUserEmail = getUserEmail(getRequestAccessToken(request));
        UserDetails userDetails = jwtuserDetailsService.loadUserByUsername(getUserEmail);
        if(userDetails != null && (request.isUserInRole("ROLE_student"))){
            List<Event> eventsListByEmail = eventRepository.findByBookingEmail(getUserEmail);
            return listMapper.mapList(eventsListByEmail, SimpleEventDTO.class,modelMapper);
        }
        else if (userDetails != null && (request.isUserInRole("ROLE_lecturer"))) {
            List<Event> eventListByCategoryOwner = eventRepository.findEventCategoryOwnerByEmail(getUserEmail);
            return listMapper.mapList(eventListByCategoryOwner , SimpleEventDTO.class,modelMapper);
        }
        return listMapper.mapList(eventsList, SimpleEventDTO.class,modelMapper);
    }

    public EventDetailDTO getEventById(Integer id, HttpServletRequest request){
        Event events = eventRepository.findById(id)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Can't get event, event id "+ id +
                        " doesn't exist."
                ));

        String getUserEmail = getUserEmail(getRequestAccessToken(request));
        if(request.isUserInRole("student")){
            if(getUserEmail.equals(events.getBookingEmail())){
                return modelMapper.map(events,EventDetailDTO.class);
            }else{
                throw new AccessDeniedException("");
//                throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Cannot view event which you didn't own");
            }
        }else if(request.isUserInRole("lecturer")){
            ArrayList<EventCategory> listCategory = new ArrayList<>();
            List<Event> eventsListByCategoryOwner = eventRepository.findEventCategoryOwnerByEmail(getUserEmail);
            System.out.println(eventsListByCategoryOwner);
            for(Event event : eventsListByCategoryOwner){
                listCategory.add(event.getEventCategory());
            }
            if(listCategory.contains(events.getEventCategory())){
                System.out.println("Yes Owner");
                return modelMapper.map(events,EventDetailDTO.class);
            }else{
                System.out.println("No owner");
                throw new AccessDeniedException("");
            }
        }
        return modelMapper.map(events,EventDetailDTO.class);
    }

    public LocalDateTime findEndDate(LocalDateTime date, Integer duration){
        return date.plusMinutes(duration);
    }

    public List<SimpleEventDTO> getAllEvent() {
        List<Event> eventList = eventRepository.findAllByOrderByEventStartTimeDesc();
        return listMapper.mapList(eventList, SimpleEventDTO.class, modelMapper);
    }

//    public Object reschedule(EventRescheduleDTO updateData, Integer id) {
//        Event existingEvent = eventRepository.findById(id).orElseThrow(()->
//                new ResponseStatusException(HttpStatus.NOT_FOUND,
//                        id + " does not exist !!!"));
//        if (!validateOverLab(updateData.getEventStartTime(), updateData.getEventCategory(), updateData.getEventDuration(), id)) {
//            return ValidationHandler.showError("eventStartTime", "Have an appointment during this time");
//        }else {
//            existingEvent.setEventStartTime(updateData.getEventStartTime());
//            existingEvent.setEventNote(updateData.getEventNote());
//            return eventRepository.saveAndFlush(existingEvent);
//        }
//    }



//    public void delete(@PathVariable Integer id) {
//        eventRepository.findById(id).orElseThrow(()->
//                new ResponseStatusException(HttpStatus.NOT_FOUND,
//                        id + " does not exist !!!"));
//        eventRepository.deleteById(id);
//    }

    public String getRequestAccessToken(HttpServletRequest request){
        return request.getHeader("Authorization").substring(7);
    }

    public String getUserEmail(String requestAccessToken){
        return jwtTokenUtil.getUsernameFromToken(requestAccessToken);
    }



//    ---------------------------------------------------------

    public Event update(Event updateEvent, Integer id) {
        Event event = eventRepository.findById(id)
                .map(o->mapEvent(o, updateEvent))
                .orElseGet(()-> {
                    updateEvent.setId(id);
                    return updateEvent;
                });
        return eventRepository.saveAndFlush(event);
    }

    private Event mapEvent(Event existingEvent , Event updateEvent){
        existingEvent.setBookingEmail(updateEvent.getBookingEmail());
        existingEvent.setEventCategory(updateEvent.getEventCategory());
        existingEvent.setBookingName(updateEvent.getBookingName());
        existingEvent.setEventDuration(updateEvent.getEventDuration());
        existingEvent.setEventStartTime(updateEvent.getEventStartTime());
        existingEvent.setEventNote(updateEvent.getEventNote());
        return existingEvent;
    }

    private Boolean validateEmail(String email) {
        String regex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(email).matches();
    }

    private Boolean validateNullInput(EventCreateDTO newEvent) {
        Boolean condition = newEvent.getBookingName() != null &&
                            newEvent.getBookingEmail() != null &&
                            newEvent.getEventCategory() != null &&
                            newEvent.getEventStartTime() != null &&
                            newEvent.getEventDuration() != null ;
        return condition;
    }

    private Boolean validateInputLength(EventCreateDTO newEvent) {
        Boolean condition;
        if(newEvent.getEventNote() == null) {
            condition = newEvent.getBookingName().length() <= 100;
        }else {
            condition = newEvent.getBookingName().length() <= 100
                    && newEvent.getBookingEmail().length() <= 100
                    && newEvent.getEventNote().length() <= 500 ;
        }
        return condition;
    }

    private Boolean validateDateFuture(LocalDateTime dateTime) {
        LocalDateTime currentDateTime = LocalDateTime.now().withSecond(0).withNano(0);
//        System.out.println(dateTime);
//        System.out.println(currentDateTime);
        if(currentDateTime.isEqual(dateTime)) {
            return false;
        }else {
            return currentDateTime.isBefore(dateTime);
        }
    }

    private List<Event> filterEvents = new ArrayList<Event>();
    private Boolean validateOverLab(LocalDateTime startDTNew, EventCategory category, Integer durationNew, Integer eventId) {
        List<Event> events = eventRepository.findAll();
        filterCategory(events, category.getEventCategoryName(), eventId);
        for (Event event : filterEvents) {
            if (!checkOverLab(startDTNew,event.getEventStartTime(), durationNew, event.getEventDuration())) {
                System.out.println("false ซ้อน");
                return false;
            }
        }
        System.out.println("true ไม่ซ้อน");
        return true;
    }

    private void filterCategory(List<Event> events, String categoryName, Integer eventId) {
        System.out.println(eventId);
        System.out.println(categoryName);
        for (Event event : events) {
            System.out.println(event.getId());
            System.out.println(eventId);
            System.out.println(event.getId().intValue() != eventId.intValue());
            System.out.println(event.getId().compareTo(eventId));
            if (event.getEventCategory().getEventCategoryName().equalsIgnoreCase(categoryName) && event.getId().compareTo(eventId) != 0) {
                filterEvents.add(event);
            }
        }
    }


    private static Boolean checkOverLab(LocalDateTime startDTNew, LocalDateTime startDTOld, Integer durationNew, Integer durationOld) {
        LocalDateTime endDTOld = startDTOld.plusMinutes(durationOld);
        LocalDateTime startDanger = startDTOld.minusMinutes(durationNew);

        if(startDTNew.isAfter(endDTOld) && !startDTNew.isEqual(endDTOld)) {
            return true;
        }else {
            if(startDTNew.isBefore(startDanger) && !startDTNew.isEqual(startDanger)) {
                return true;
            }
        }
        System.out.println("false ซ้อน");
        return false;

    }

}