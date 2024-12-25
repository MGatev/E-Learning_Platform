package course.spring.elearningplatform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@AllArgsConstructor
@NoArgsConstructor(force = true)
@Data
public class ImageDto {
  private MultipartFile image;

  public byte[] getImageBytes() {
    if (!image.isEmpty()) {
      try {
        return image.getBytes();
      } catch (IOException e) {
        throw new RuntimeException("Error uploading image!", e);
      }
    }

    return null;
  }

  public String getMimeType() {
    return image.getContentType(); // Extract MIME type directly from MultipartFile
  }
}
