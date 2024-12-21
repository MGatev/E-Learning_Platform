package course.spring.elearningplatform.service;

import course.spring.elearningplatform.dto.ImageDto;
import course.spring.elearningplatform.entity.Image;

public interface ImageService {

  Image getImageById(Long id);
  Image createImage(ImageDto image);
}
