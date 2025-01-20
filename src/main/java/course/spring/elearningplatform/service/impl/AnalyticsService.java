package course.spring.elearningplatform.service.impl;

import course.spring.elearningplatform.entity.Course;
import course.spring.elearningplatform.entity.CourseAnalytics;
import course.spring.elearningplatform.entity.StudentResult;
import course.spring.elearningplatform.repository.AnalyticsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnalyticsService {
    private final AnalyticsRepository analyticsRepository;

    public AnalyticsService(AnalyticsRepository analyticsRepository) {
        this.analyticsRepository = analyticsRepository;
    }

    public CourseAnalytics addAnalytics(CourseAnalytics analytics) {
        return analyticsRepository.save(analytics);
    }

    public void addNewParticipantAnalytics(Course course, StudentResult newStudentResult, List<StudentResult> highScores) {
        highScores.forEach(System.out::println);
        CourseAnalytics courseAnalytics = course.getAnalytics();
        courseAnalytics.setTotalParticipants(courseAnalytics.getTotalParticipants() + 1);
        System.out.println("Total participants: " + courseAnalytics.getTotalParticipants());
        courseAnalytics.setAveragePercentage(getNewAveragePercentage(highScores));
        System.out.println("Average percentage: " + courseAnalytics.getAveragePercentage());
        if (courseAnalytics.getFastestTime() > newStudentResult.getElapsedTime()) {
            courseAnalytics.setFastestTime(newStudentResult.getElapsedTime());
        }
        System.out.println("Fastest time: " + courseAnalytics.getFastestTime());
        courseAnalytics.setAverageTime(getNewAverageTime(highScores));
        System.out.println("Average time: " + courseAnalytics.getAverageTime());
        analyticsRepository.save(courseAnalytics);
    }

    public void addNewHighScoreInAnalytics(Course course, StudentResult newStudentResult, List<StudentResult> highScores) {
        highScores.forEach(System.out::println);
        CourseAnalytics courseAnalytics = course.getAnalytics();

        courseAnalytics.setAveragePercentage(getNewAveragePercentage(highScores));
        System.out.println("Average percentage: " + courseAnalytics.getAveragePercentage());
        long fastestTime = getFastestTime(newStudentResult.getElapsedTime(), highScores);
        courseAnalytics.setFastestTime(fastestTime);
        System.out.println("Fastest time: " + courseAnalytics.getFastestTime());
        courseAnalytics.setAverageTime(getNewAverageTime(highScores));
        System.out.println("Average time: " + courseAnalytics.getAverageTime());
        analyticsRepository.save(courseAnalytics);
    }

    private long getFastestTime(long newTime, List<StudentResult> highScores) {
        return highScores.stream()
            .mapToLong(StudentResult::getElapsedTime)
            .min()
            .orElse(newTime);
    }

    private double getNewAveragePercentage(List<StudentResult> highScores) {
        return highScores.stream()
            .mapToDouble(StudentResult::getPercentage)
            .average()
            .orElse(0.0);
    }

    private double getNewAverageTime(List<StudentResult> highScores) {
        return highScores.stream()
            .mapToDouble(StudentResult::getElapsedTime)
            .average()
            .orElse(0.0);
    }
}
