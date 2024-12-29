package course.spring.elearningplatform.repository;

import course.spring.elearningplatform.entity.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CertificateRepository extends JpaRepository<Certificate, Long> {
}
