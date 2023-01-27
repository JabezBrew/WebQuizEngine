package engine.controllers;

import engine.services.QuizService;
import engine.security.UserDetailsImpl;
import engine.entities.CompletedQuiz;
import engine.entities.Quiz;
import engine.repos.CompletedQuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class QuizController {

    QuizService quizService;

    CompletedQuizRepository completedQuizRepo;
    @Autowired
    public QuizController(QuizService quizService, CompletedQuizRepository completedQuizRepo) {
        this.quizService = quizService;
        this.completedQuizRepo = completedQuizRepo;
    }


    @GetMapping("/quizzes")
    public Page<Quiz> getQuizzes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize) {

        Long userId = UserDetailsImpl.getCurrentUserID();

        int x = 1;
        int totalElement = (int) completedQuizRepo.countByUserId(userId);

        if (page == 0) {
            x = 0;
        }else if (totalElement > 10){
            page = totalElement / pageSize + 1;
        }

        PageRequest request = PageRequest.of(page - x, pageSize);
        return quizService.findAllQuizzes(request);
    }

    @GetMapping("/quizzes/{id}")
    public Quiz getQuiz(@PathVariable int id) {
        return quizService.getQuizById(id);
    }

    @GetMapping("/quizzes/completed")
    public Page<CompletedQuiz> getCompletedQuizzes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "completedAt") String sortBy) {

        Long userId = UserDetailsImpl.getCurrentUserID();
        int x = 1;
        int totalElement = (int) completedQuizRepo.countByUserId(userId);

        if (page == 0) {
            x = 0;
        }else if (totalElement > 10){
            page = totalElement / pageSize + 1;
        }

        PageRequest request = PageRequest.of(page - x, pageSize, Sort.by(sortBy).descending().and(Sort.by("id").descending()));
        return completedQuizRepo.findQuizzesCompletedByUser(userId, request);
    }

    @PostMapping("/quizzes")
    public Quiz postQuiz(@Valid @RequestBody Quiz quiz) {
        quiz.setUser(UserDetailsImpl.getCurrentUser());
        return quizService.saveQuiz(quiz);
    }

    @PostMapping("/quizzes/{id}/solve")
    public QuizService.AnswerCheck postAnswer(@PathVariable int id, @Valid  @RequestBody QuizService.Answer answer) {
        return quizService.checkAnswer(answer, id);
    }


    @DeleteMapping("/quizzes/{id}")
    public ResponseEntity<Object> deleteQuiz(@PathVariable int id) {
        return quizService.deleteQuiz(id);
    }
}
