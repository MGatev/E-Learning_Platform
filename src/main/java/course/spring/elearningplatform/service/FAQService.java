package course.spring.elearningplatform.service;

import course.spring.elearningplatform.dto.FAQDto;
import course.spring.elearningplatform.entity.FAQ;

import java.util.List;

public interface FAQService {
    FAQ addQuestion(FAQDto faqDto);

    List<FAQ> getAllQuestions();

    FAQ deleteQuestion(Long id);
}
