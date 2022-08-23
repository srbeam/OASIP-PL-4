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
    private EventCategoryService service;

    @GetMapping("")
    public List<EventCategory> getAllCategory() {
        return service.getAllCategory();
    }

    @GetMapping("/{id}")
    public EventCategory getEventById(@PathVariable Integer id) {
        return service.getCategoryById(id);
    }

    @PutMapping("/{id}")
    public Object updateCategory(@Valid @RequestBody SimpleEventCategoryDTO updateCategory, @PathVariable Integer id) {
        return service.updateCategory(updateCategory, id);
    }

}
