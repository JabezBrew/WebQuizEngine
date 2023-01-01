package engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("/api")
public class QuizController {
    @Autowired //if removed, code doesn't work.
    QuizService quizService;
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
    public Quiz postQuiz(@Valid @RequestBody Quiz quiz) {
        return quizService.saveQuiz(quiz);
    }

    @PostMapping("/quizzes/{id}/solve")
    public AnswerCheck postAnswer(@PathVariable int id, @Valid  @RequestBody Answer answer) {
        Quiz quiz = quizService.getQuizById(id);
        if (quiz.answer() == null && answer.answer().length == 0) {
            return correctAnswer;
        } else {
            return Arrays.equals(answer.answer(), quiz.answer()) ? correctAnswer : wrongAnswer;
        }
    }

    @DeleteMapping("quizzes")
    public void deleteAllQuizzes() {
        quizService.deleteAllQuizzes();
        System.out.println("All quizzes deleted");
    }
}
