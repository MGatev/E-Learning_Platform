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

    public Map<User, ProgressInfo> getUserProgressInCourse(Long courseId) {
        // Fetch the course and its lessons
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));
        List<Lesson> lessons = course.getLessons();
        long totalLessons = lessons.size();

        Map<User, ProgressInfo> userProgress = new HashMap<>();

        // Loop over each student
        for (User student : course.getParticipants()) {
            Set<Lesson> studentLessons = student.getCompletedLessons();

            // Get the lessons that are part of the course
            List<Lesson> courseLessons = studentLessons.stream()
                    .filter(lesson -> lesson.getRelatedCourse() != null && lesson.getRelatedCourse().getId().equals(courseId))
                    .collect(Collectors.toList());

            // Count the completed lessons for the current course
            long completedLessons = courseLessons.size();

            // Calculate the progress bar width
            double progressPercentage = totalLessons > 0 ? (completedLessons * 100.0) / totalLessons : 0;

            // Round the percentage to 2 decimal places
            double roundedProgressPercentage = Math.round(progressPercentage * 100.0) / 100.0;


            // Store the progress in a custom object
            userProgress.put(student, new ProgressInfo(completedLessons, totalLessons, roundedProgressPercentage));
        }

        return userProgress;
    }

    // Helper class to store progress information
    public static class ProgressInfo {
        private final long completedLessons;
        private final long totalLessons;
        private final double progressWidth;

        public ProgressInfo(long completedLessons, long totalLessons, double progressWidth) {
            this.completedLessons = completedLessons;
            this.totalLessons = totalLessons;
            this.progressWidth = progressWidth;
        }

        public long getCompletedLessons() {
            return completedLessons;
        }

        public long getTotalLessons() {
            return totalLessons;
        }

        public double getProgressWidth() {
            return progressWidth;
        }
    }
}
