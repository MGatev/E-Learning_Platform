package course.spring.elearningplatform.web;

import course.spring.elearningplatform.entity.Image;
import course.spring.elearningplatform.entity.User;
import course.spring.elearningplatform.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.io.InputStream;

@Controller
@RequestMapping("/api/images")
public class ImageController {
    private final UserService userService;

    public ImageController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<byte[]> getProfilePicture(@PathVariable String username) {
        User user = userService.getUserByUsername(username);

        if (user == null || user.getProfilePicture() == null) {
            byte[] defaultImage = loadDefaultImage(); // Load a default image from resources
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG) // Adjust MIME type based on the default image
                    .body(defaultImage);
        }

        Image profilePicture = user.getProfilePicture();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(profilePicture.getMimeType())) // Use MIME type
                .body(profilePicture.getImage()); // Return the image bytes
    }

    private byte[] loadDefaultImage() {
        try (InputStream is = getClass().getResourceAsStream("/static/images/default-profile.jpg")) {
            return is != null ? is.readAllBytes() : new byte[0];
        } catch (IOException e) {
            throw new RuntimeException("Failed to load default profile picture", e);
        }
    }
}
