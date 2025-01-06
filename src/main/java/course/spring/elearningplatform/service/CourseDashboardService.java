package course.spring.elearningplatform.service;

import course.spring.elearningplatform.entity.User;

import java.util.Map;

public interface CourseDashboardService {
    public Map<User, Double> getUserProgressInCourse(Long courseId);
}
