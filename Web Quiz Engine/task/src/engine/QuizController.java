package engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


@RestController
@RequestMapping("/api")
public class QuizController {

    QuizService quizService;
    @Autowired
    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }
    public record AnswerCheck(boolean success, String feedback) {}
    public record Answer(int[] answer) {}
    AnswerCheck correctAnswer = new AnswerCheck(true, "Congratulations, you are right!");
    AnswerCheck wrongAnswer = new AnswerCheck(false, "Wrong answer! Please try again.");

    @GetMapping("/quizzes")
    public List<Quiz> getQuizzes() {
        return quizService.getAllQuizzes();
    }

    @GetMapping("/quizzes/{id}")
    public Quiz getQuiz(@PathVariable int id) {
        return quizService.getQuizById(id);
    }

    @PostMapping("/quizzes")
    public Quiz postQuiz(@Valid @RequestBody Quiz quiz, Authentication auth) {
        UserDetailsImpl currentUser = (UserDetailsImpl) auth.getPrincipal();
        Long userId = currentUser.getId();
        User user = new User();
        user.setId(userId);
        quiz.setUser(user);
        
        return quizService.saveQuiz(quiz);
    }

    @PostMapping("/quizzes/{id}/solve")
    public AnswerCheck postAnswer(@PathVariable int id, @Valid  @RequestBody Answer answer) {
        Quiz quiz = quizService.getQuizById(id);
        if (quiz.getAnswer() == null && answer.answer().length == 0) {
            return correctAnswer;
        } else {
            return Arrays.equals(answer.answer(), quiz.getAnswer()) ? correctAnswer : wrongAnswer;
        }
    }

    @DeleteMapping("/quizzes")
    public void deleteAllQuizzes() {
        quizService.deleteAllQuizzes();
        System.out.println("All quizzes deleted");
    }

    @DeleteMapping("/quizzes/{id}")
    public ResponseEntity<Quiz> deleteQuiz(@PathVariable int id, Authentication auth) {
        UserDetailsImpl currentUser = (UserDetailsImpl) auth.getPrincipal();
        Long userId = currentUser.getId();
        Quiz quiz = quizService.getQuizById(id);
        if (Objects.equals(quiz.getUser().getId(), userId)) {
            quizService.deleteQuiz(id);
            return ResponseEntity.status(204).build();
        } else {
            throw new ForbiddenException();
        }
    }
}
