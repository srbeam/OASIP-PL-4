package sit.int221.oasipbackend.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.oasipbackend.dtos.*;
import sit.int221.oasipbackend.entities.Event;
import sit.int221.oasipbackend.entities.EventCategory;
import sit.int221.oasipbackend.exceptions.ValidationHandler;
import sit.int221.oasipbackend.repositories.EventCategoryRepository;
import sit.int221.oasipbackend.repositories.EventRepository;
import sit.int221.oasipbackend.utils.ListMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;


    @Autowired
    private EventCategoryRepository eventCategoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ListMapper listMapper;

    public List<SimpleEventDTO> getAllEvent() {
        Sort sort = Sort.by("eventStartTime");
        List<Event> eventList = eventRepository.findAll(sort.descending());
        return listMapper.mapList(eventList, SimpleEventDTO.class, modelMapper);
    }

    public EventPageDTO getAllEventPage(int page, int pageSize, String sortBy) {
        Sort sort = Sort.by(sortBy);
        return modelMapper.map(eventRepository.findAll(
                        PageRequest.of(page, pageSize, sort.descending())),
                EventPageDTO.class);
    }

    public EventDetailDTO getEventById(Integer id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        id + " Dose not exits!!!"));
        return modelMapper.map(event, EventDetailDTO.class);
    }

    public Object save(EventCreateDTO newEvent) {
        if(!validateOverLab(newEvent.getEventStartTime(), newEvent.getEventCategory(), newEvent.getEventDuration(), 0)) {
            System.out.println();
//            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Have an appointment during this time");
            return ValidationHandler.showError("eventStartTime", "Have an appointment during this time");
        }else {
            Event event = modelMapper.map(newEvent, Event.class);
            return eventRepository.saveAndFlush(event);
        }
    }

    public void delete(@PathVariable Integer id) {
        eventRepository.findById(id).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        id + " does not exist !!!"));
        eventRepository.deleteById(id);
    }

    public Object reschedule(EventRescheduleDTO updateData, Integer id) {
        if (!validateOverLab(updateData.getEventStartTime(), updateData.getEventCategory(), updateData.getEventDuration(), id)) {
//            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Have an appointment during this time");
            return ValidationHandler.showError("eventStartTime", "Have an appointment during this time");
        }else {
            Event existingEvent = eventRepository.findById(id).orElseThrow(()->
                    new ResponseStatusException(HttpStatus.NOT_FOUND,
                            id + " does not exist !!!"));
            existingEvent.setEventStartTime(updateData.getEventStartTime());
            existingEvent.setEventNote(updateData.getEventNote());
            return eventRepository.saveAndFlush(existingEvent);
        }
    }

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
        System.out.println(filterEvents);

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