package course.spring.elearningplatform.dto.mapper;

import course.spring.elearningplatform.dto.CourseDto;
import course.spring.elearningplatform.entity.Course;
import course.spring.elearningplatform.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

public class CourseDtoToCourseMapper {
    public static Course mapCourseDtoToCourse(CourseDto courseDto, User user) {
        Course course = new Course();
        course.setName(courseDto.getName());
        course.setDescription(courseDto.getDescription());
        course.setCategories(courseDto.getCategories());
        course.setCreatedBy(user);
        course.setCreatedOn(new Date());
        MultipartFile image = courseDto.getImage();
        if (!image.isEmpty()) {
            try {
                course.setImage(image.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Error uploading image!", e);
            }
        }
        return course;
    }
}