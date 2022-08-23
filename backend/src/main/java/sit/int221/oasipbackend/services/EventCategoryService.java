package sit.int221.oasipbackend.services;

import jdk.jfr.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.oasipbackend.dtos.EventDetailDTO;
import sit.int221.oasipbackend.dtos.SimpleEventCategoryDTO;
import sit.int221.oasipbackend.entities.EventCategory;
import sit.int221.oasipbackend.exceptions.ValidationHandler;
import sit.int221.oasipbackend.repositories.EventCategoryRepository;
import sit.int221.oasipbackend.repositories.EventRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventCategoryService {
    @Autowired
    private EventCategoryRepository repository;

    public List<EventCategory> getAllCategory() {
        List<EventCategory> eventList = repository.findAll();
        return eventList;
    }

    public EventCategory getCategoryById(Integer id) {
        return repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, id + " Dose not exits!!!"));
    }

    public Object updateCategory(SimpleEventCategoryDTO updateCategory, Integer categoryId) {
        if(!validateDuplicateName(updateCategory.getEventCategoryName(), categoryId)){
//            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "the eventCategoryName is NOT unique.");
            return ValidationHandler.showError("eventCategoryName", "The eventCategoryName is NOT unique");
        }else {
            EventCategory existingCategory = repository.findById(categoryId).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, categoryId + " Dose not exits!!!"));

            existingCategory.setEventCategoryName(updateCategory.getEventCategoryName());
            existingCategory.setEventCategoryDescription(updateCategory.getEventCategoryDescription());
            existingCategory.setEventDuration(updateCategory.getEventDuration());
            return repository.saveAndFlush(existingCategory);
        }
    }

    private ArrayList<String> categoryNameAll = new ArrayList<String>();
    public Boolean validateDuplicateName(String categoryName, Integer id) {
        List<EventCategory> categoryList = repository.findAll();
        for (EventCategory category : categoryList) {
            if (category.getId().intValue() != id.intValue()) {
                categoryNameAll.add(category.getEventCategoryName());
            }
        }
        return !categoryNameAll.contains(categoryName);

    }

}
