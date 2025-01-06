package course.spring.elearningplatform.service.impl;

import course.spring.elearningplatform.entity.Course;
import course.spring.elearningplatform.entity.Lesson;
import course.spring.elearningplatform.entity.User;
import course.spring.elearningplatform.repository.CourseRepository;
import course.spring.elearningplatform.repository.UserRepository;
import course.spring.elearningplatform.service.CourseDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CourseDashboardServiceImpl implements CourseDashboardService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    public Map<User, Double> getUserProgressInCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        List<Lesson> lessons = course.getLessons();
        long totalLessons = lessons.size();

        Map<User, Double> userProgress = new HashMap<>();

        for (User student : userRepository.findAll()) {
            Set<Lesson> studentLessons = student.getCompletedLessons();

            List<Lesson> courseLessons = studentLessons.stream()
                    .filter(lesson -> lesson.getRelatedCourse() != null && lesson.getRelatedCourse().getId().equals(courseId))
                    .collect(Collectors.toList());

            long completedLessons = courseLessons.stream()
                    .filter(student.getCompletedLessons()::contains)
                    .count();

            double progressPercentage = totalLessons > 0 ? (completedLessons * 100.0) / totalLessons : 0;

            userProgress.put(student, progressPercentage);
        }

        return userProgress;
    }
}

