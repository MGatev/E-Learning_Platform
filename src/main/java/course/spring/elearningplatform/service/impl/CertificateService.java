package course.spring.elearningplatform.service.impl;

import course.spring.elearningplatform.entity.Certificate;
import course.spring.elearningplatform.entity.Course;
import course.spring.elearningplatform.entity.User;
import course.spring.elearningplatform.repository.CertificateRepository;
import course.spring.elearningplatform.service.UserService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
public class CertificateService {
    private final CertificateRepository certificateRepository;
    private final UserService userService;

    public CertificateService(CertificateRepository certificateRepository, UserService userService) {
        this.certificateRepository = certificateRepository;
        this.userService = userService;
    }

    public void issueCertificate(String username, Course course, int scorePercentage) {
        if (scorePercentage >= 80) {
            User user = userService.getUserByUsername(username);

            Certificate certificate = new Certificate();
            certificate.setCourseName(course.getName());
            certificate.setIssuedTo(user);
            certificate.setScorePercentage(scorePercentage);
            certificate.setIssuedOn(Date.from(Instant.now()));

            Certificate savedCertificate = certificateRepository.save(certificate);
            user.addCertificate(savedCertificate);
            userService.save(user);
        }
    }
}
