package sit.int221.oasipbackend.controllers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sit.int221.oasipbackend.services.FileStorageService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/api/downloadFile")
public class FileController {
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private  FileController fileController;

    @GetMapping("/{id}/{fileCode}")
//    filecode = namefile download

//    download อยู่ในรูปแบบ opties stream -> media type
    public ResponseEntity<Resource> downloadfile(@PathVariable Integer id , @PathVariable String fileCode) {
        Resource file = fileStorageService.loadFileAsResource(fileCode , id);
        String contentType = "application/octet-stream";
        String headerValue =  "attachment; filename=\"" + file.getFilename() + "\"";
//    pattern downloadfile
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).
                header(HttpHeaders.CONTENT_DISPOSITION , headerValue).body(file);
    }
}
