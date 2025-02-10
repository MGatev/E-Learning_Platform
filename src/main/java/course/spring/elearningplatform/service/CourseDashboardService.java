package course.spring.elearningplatform.service;

import course.spring.elearningplatform.entity.User;
import course.spring.elearningplatform.service.impl.CourseDashboardServiceImpl;

import java.util.Map;

public interface CourseDashboardService {
    public Map<User, CourseDashboardServiceImpl.ProgressInfo> getUserProgressInCourse(Long courseId);
}
