package course.spring.elearningplatform.repository;

import course.spring.elearningplatform.entity.StudentResult;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface StudentResultRepository extends JpaRepository<StudentResult, String> {

    @Modifying
    @Transactional
    @Query("UPDATE StudentResult sr SET sr.percentage = :percentage WHERE sr.username = :username")
    int updateStudentResult(@Param("username") String username, @Param("percentage") int percentage);
}

