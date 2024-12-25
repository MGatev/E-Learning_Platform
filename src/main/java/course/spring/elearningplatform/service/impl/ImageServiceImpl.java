package course.spring.elearningplatform.service.impl;

import course.spring.elearningplatform.dto.ImageDto;
import course.spring.elearningplatform.entity.Image;
import course.spring.elearningplatform.exception.EntityNotFoundException;
import course.spring.elearningplatform.repository.ImageRepository;
import course.spring.elearningplatform.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageServiceImpl implements ImageService {

  private final ImageRepository imageRepository;

  @Autowired
  public ImageServiceImpl(ImageRepository imageRepository) {
    this.imageRepository = imageRepository;
  }

  @Override
  public Image getImageById(Long id) {
    return imageRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(String.format("Image with id %s not found", id), "redirect:/groups"));
  }

  @Override
  public Image createImage(ImageDto imageDto) {
    Image imageToBeSaved = new Image(imageDto.getImageBytes(), imageDto.getMimeType());
    return imageRepository.save(imageToBeSaved);
  }
}
