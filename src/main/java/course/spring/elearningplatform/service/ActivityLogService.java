package course.spring.elearningplatform.service;

import course.spring.elearningplatform.entity.ActivityLog;

import java.util.Map;

public interface ActivityLogService {
    Map<ActivityLog, String> getAllLogs();
    void logActivity(String action, String username);
}
