package sit.int221.oasipbackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sit.int221.oasipbackend.dtos.EventDetailDTO;
import sit.int221.oasipbackend.dtos.SimpleEventCategoryDTO;
import sit.int221.oasipbackend.entities.EventCategory;
import sit.int221.oasipbackend.services.EventCategoryService;
import sit.int221.oasipbackend.services.EventService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin
public class EventCategoryController {
    @Autowired
    EventCategoryService eventCategoryService;

    @Autowired
    EventCategoryService eventService;


    @GetMapping("")
    public List<EventCategory> getAllCategory() {
        return eventCategoryService.getAllCategory();
    }

    @GetMapping("/{id}")
    public EventCategory getCategoryById(@PathVariable Integer id) {
        return eventCategoryService.getCategoryById(id);
    }

    @PutMapping("/{id}")
    public Object updateCategory(@Valid @RequestBody SimpleEventCategoryDTO updateCategory, @PathVariable Integer id) {
        return eventCategoryService.updateCategory(updateCategory, id);
    }

}
