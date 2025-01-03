package course.spring.elearningplatform.service.impl;

import course.spring.elearningplatform.entity.Question;
import course.spring.elearningplatform.entity.Quiz;
import course.spring.elearningplatform.entity.QuizDto;
import course.spring.elearningplatform.entity.Response;
import course.spring.elearningplatform.entity.StudentResult;
import course.spring.elearningplatform.exception.EntityNotFoundException;
import course.spring.elearningplatform.repository.QuizRepository;
import course.spring.elearningplatform.repository.StudentResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class QuizzesService {
    private final QuizRepository quizRepository;
    private final StudentResultRepository studentResultRepository;

    @Autowired
    public QuizzesService(QuizRepository quizRepository,
                          StudentResultRepository studentResultRepository) {
        this.quizRepository = quizRepository;
        this.studentResultRepository = studentResultRepository;
    }

    public Quiz createQuiz(QuizDto quizDto, List<Question> quizQuestions) {
        if (quizQuestions.isEmpty()) {
            throw new EntityNotFoundException("There are no questions for creating a quiz. Try adding some.");
        }

        var quiz = new Quiz();
        quiz.setTitle(quizDto.getTitle());

        Collections.shuffle(quizQuestions);
        List<Question> selectedQuestions = quizQuestions.stream()
            .limit(quizDto.getNumberOfQuestions())
            .toList();

        quiz.setQuestions(new ArrayList<>(selectedQuestions));

        return quizRepository.save(quiz);
    }

    public Quiz getQuizById(long id) {
        //todo fix redirect url
        return quizRepository.findById(id)
            .orElseThrow(
                () -> new EntityNotFoundException(String.format("Quiz with id %s not found", id), "redirect:/groups"));

    }

    public ResponseEntity<Map<String, Integer>> calculateQuizResult(long id, List<Response> answers) {
        Optional<Quiz> quizOptional = quizRepository.findById(id);
        if (quizOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        var quiz = quizOptional.get();
        List<Question> questionsDB = quiz.getQuestions();

        int rightAnswers =
            Math.toIntExact(answers.stream().filter(answer -> isCorrectAnswer(answer, questionsDB)).count());

        Map<String, Integer> result = new HashMap<>();
        result.put("score", rightAnswers);  // Correct answers
        result.put("totalQuestions", questionsDB.size());  // Total questions

        int percentage = Math.toIntExact(Math.round((rightAnswers * 100.0) / questionsDB.size()));
        result.put("percentage", percentage);

        addNewStudentResult(percentage, quiz);

        return ResponseEntity.ok(result);
    }


    private boolean isCorrectAnswer(Response answer, List<Question> questions) {
        return questions.stream()
            .filter(question -> question.getId() == answer.getQuestionId())
            .findFirst()
            .map(question -> question.getCorrectAnswer().equals(answer.getAnswer()))
            .orElse(false);
    }

    private boolean isNewStudentRecord(int currentPercentage, String username, List<StudentResult> highScores) {
        return highScores.stream()
            .filter(score -> score.getUsername().equals(username))
            .map(score -> score.getPercentage())
            .anyMatch(percent -> percent < currentPercentage);
    }

    private void addNewStudentResult(int newPercentage, Quiz quiz) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();

        if (!studentResultRepository.existsById(username)) {
            var studentResult = studentResultRepository.save(new StudentResult(username, newPercentage));
            quiz.getHighScores().add(studentResult);
            quizRepository.save(quiz);
            return;
        }

        if (isNewStudentRecord(newPercentage, username, quiz.getHighScores())) {
            studentResultRepository.updateStudentResult(username, newPercentage);
        }
    }


}
