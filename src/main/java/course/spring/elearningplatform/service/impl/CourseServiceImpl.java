package course.spring.elearningplatform.service.impl;

import course.spring.elearningplatform.dto.CourseDto;
import course.spring.elearningplatform.dto.mapper.CourseDtoToCourseMapper;
import course.spring.elearningplatform.entity.Course;
import course.spring.elearningplatform.entity.Lesson;
import course.spring.elearningplatform.entity.User;
import course.spring.elearningplatform.exception.DuplicatedEntityException;
import course.spring.elearningplatform.exception.EntityNotFoundException;
import course.spring.elearningplatform.repository.CourseRepository;
import course.spring.elearningplatform.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public Course addCourse(CourseDto courseDto, User user) {
        if (courseRepository.existsByName(courseDto.getName())) {
            throw new DuplicatedEntityException(String.format(
                    "Cannot create a course with name '%s' because it already exists. Please choose other name.", courseDto.getName()));
        }

        Course course = CourseDtoToCourseMapper.mapCourseDtoToCourse(courseDto, user);
        return courseRepository.save(course);
    }

    @Override
    public Map<String, List<Course>> getCoursesGroupedByCategory() {
        List<Course> courses = courseRepository.findAll();

        return courses.stream()
                .flatMap(course -> course.getCategories().stream()
                        .map(category -> new AbstractMap.SimpleEntry<>(category, course)))
                .collect(Collectors.groupingBy(
                        AbstractMap.SimpleEntry::getKey,
                        Collectors.mapping(AbstractMap.SimpleEntry::getValue, Collectors.toList())
                ));
    }

    @Override
    public Course getCourseById(Long id) {
        return courseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("There is no such course!"));
    }

    @Override
    public boolean areAllLessonsCompletedByUser(User user, Course course) {
        Set<Lesson> completedLessons = user.getCompletedLessons();
        List<Lesson> courseLessons = course.getLessons();

        for (Lesson lesson : courseLessons) {
            if (!completedLessons.contains(lesson)) {
                return false;
            }
        }

        return true;
    }
}
