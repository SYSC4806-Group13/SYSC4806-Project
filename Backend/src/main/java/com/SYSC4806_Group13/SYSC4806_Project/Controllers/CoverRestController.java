package com.SYSC4806_Group13.SYSC4806_Project.Controllers;

import com.SYSC4806_Group13.SYSC4806_Project.Exceptions.InvalidFileTypeException;
import org.apache.commons.io.FileUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = {"*"}, maxAge = 4800, allowCredentials = "false")
@RestController
public class CoverRestController {

    private final String FILE_PATH_ROOT = System.getProperty("user.dir") + "/covers/";

    @GetMapping("/covers/{listingId}")
    public ResponseEntity<byte[]> getImage(@PathVariable String listingId) {
        byte[] image = new byte[0];
        try {
            File filePng = new File(FILE_PATH_ROOT + listingId + ".png");
            File fileJpg = new File(FILE_PATH_ROOT + listingId + ".jpg");
            if (filePng.exists()) {
                image = FileUtils.readFileToByteArray(filePng);
            }
            if (fileJpg.exists()) {
                image = FileUtils.readFileToByteArray(fileJpg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(image);
    }

    @PostMapping("/covers/{listingId}")
    public Map<String, Object> uploadImage(@RequestParam("imageFile") MultipartFile imageFile,
                                           @PathVariable String listingId) {
        Map<String, Object> map = new HashMap<>();
        if (imageFile.getContentType() != null) {
            if (!isSupportedContentType(imageFile.getContentType())) {
                throw new InvalidFileTypeException("File can only be of type: [png, jpg, jpeg]");
            }
        } else {
            throw new InvalidFileTypeException("File can only be of type: [png, jpg, jpeg]");
        }

        try {
            byte[] bytes = imageFile.getBytes();

            // Ensure rootDirectory to save covers to is created
            Path rootDirectory = Paths.get(FILE_PATH_ROOT);
            Files.createDirectories(rootDirectory);

            // Delete previous cover
            deletePreviousCover(FILE_PATH_ROOT + listingId);

            // Save the new cover
            Path filePath = Paths.get(FILE_PATH_ROOT +
                    listingId +
                    contentTypeToFileTypeMapping(imageFile.getContentType()));
            Files.write(filePath, bytes);

        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("listingId", listingId);
        return map;
    }

    private void deletePreviousCover(String filePath) {
        try {
            Files.delete(Paths.get(filePath + ".png"));
        } catch (NoSuchFileException e) {
            // Can ignore
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Files.delete(Paths.get(filePath + ".jpg"));
        } catch (NoSuchFileException e) {
            // Can ignore
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String contentTypeToFileTypeMapping(String contentType) {
        return switch (contentType) {
            case "image/png" -> ".png";
            case "image/jpg", "image/jpeg" -> ".jpg";
            default -> "";
        };
    }

    private boolean isSupportedContentType(String contentType) {
        return contentType.equals("image/png")
                || contentType.equals("image/jpg")
                || contentType.equals("image/jpeg");
    }
}
