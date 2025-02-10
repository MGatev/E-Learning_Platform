package course.spring.elearningplatform.repository;

import course.spring.elearningplatform.entity.StudentResult;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface StudentResultRepository extends JpaRepository<StudentResult, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE StudentResult sr SET sr.percentage = :percentage, sr.elapsedTime = :elapsedTime WHERE sr.id = :id")
    int updateStudentResult(@Param("id") long id, @Param("percentage") int percentage, @Param("elapsedTime") long elapsedTime);
}

