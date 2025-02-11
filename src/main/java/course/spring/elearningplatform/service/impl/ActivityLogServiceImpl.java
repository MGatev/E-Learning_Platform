package course.spring.elearningplatform.service.impl;

import course.spring.elearningplatform.entity.ActivityLog;
import course.spring.elearningplatform.entity.Article;
import course.spring.elearningplatform.repository.ActivityLogRepository;
import course.spring.elearningplatform.service.ActivityLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ActivityLogServiceImpl implements ActivityLogService {
    private final ActivityLogRepository activityLogRepository;

    @Autowired
    public ActivityLogServiceImpl(ActivityLogRepository activityLogRepository) {
        this.activityLogRepository = activityLogRepository;
    }

    @Override
    public Map<ActivityLog, String> getAllLogs() {
        List<ActivityLog> logs = activityLogRepository.findAll(Sort.by(Sort.Order.desc("timestamp")));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy h:mm a");
        Map<ActivityLog, String> logsMap = new HashMap<>();

        for (ActivityLog log : logs) {
            String formattedDate = log.getTimestamp().format(formatter);
            logsMap.put(log, formattedDate);
        }

        return logsMap;
    }

    @Override
    public void logActivity(String action, String username) {
        ActivityLog log = new ActivityLog();
        log.setAction(action);
        log.setUsername(username);
        log.setTimestamp(LocalDateTime.now());
        activityLogRepository.save(log);
    }
}
