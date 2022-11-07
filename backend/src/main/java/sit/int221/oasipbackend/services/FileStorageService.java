package sit.int221.oasipbackend.services;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import sit.int221.oasipbackend.exceptions.FileStorageException;
import sit.int221.oasipbackend.exceptions.MyFileNotFoundException;
import sit.int221.oasipbackend.properties.FileStorageProperties;

import java.io.IOException;
import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
@Service
public class FileStorageService {
    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String storeFile(MultipartFile file , Integer id) throws IOException {

//      Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

//      call id -> file
        Path getpathbyid = Paths.get(String.valueOf(this.fileStorageLocation)+ "/" + id);

//      create New Dircertoty
        Files.createDirectories(getpathbyid);

        System.out.println(fileName);
        System.out.println(getpathbyid);

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

//          Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = getpathbyid.resolve(fileName);
//          see targetLocation file
            System.out.println(targetLocation);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Resource loadFileAsResource(String fileName , Integer id) {
        try {
            Path filePath = this.fileStorageLocation.resolve(id.toString()).resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }
}
