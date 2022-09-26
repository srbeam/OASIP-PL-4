package sit.int221.oasipbackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import sit.int221.oasipbackend.services.RefreshService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/refresh")
public class RefreshController {

    private final RefreshService refreshService;

    @Autowired
    public RefreshController(RefreshService refreshService) {
        this.refreshService = refreshService;
    }

//        @PostMapping("")
//        @ResponseStatus(code = HttpStatus.OK)
//        public ResponseEntity refreshLogin(HttpServletRequest request) throws Exception {
//            return refreshService.refreshLogin(request);
//        }

    @GetMapping("")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity refreshLogin(@Valid HttpServletRequest request){
        return refreshService.refreshToken(request);
    }
}