package engine.controllers;

import engine.exceptions.ForbiddenException;
import engine.services.QuizService;
import engine.security.UserDetailsImpl;
import engine.entities.CompletedQuiz;
import engine.entities.Quiz;
import engine.entities.User;
import engine.repos.CompletedQuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;


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
    public record AnswerCheck(boolean success, String feedback) {}
    public record Answer(int[] answer) {}
    AnswerCheck correctAnswer = new AnswerCheck(true, "Congratulations, you are right!");
    AnswerCheck wrongAnswer = new AnswerCheck(false, "Wrong answer! Please try again.");

    @GetMapping("/quizzes")
    public Page<Quiz> getQuizzes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            Authentication auth) {

        UserDetailsImpl currentUser = (UserDetailsImpl) auth.getPrincipal();
        Long userId = currentUser.getId();

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
            @RequestParam(defaultValue = "completedAt") String sortBy,
            Authentication auth) {

        UserDetailsImpl currentUser = (UserDetailsImpl) auth.getPrincipal();
        Long userId = currentUser.getId();

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
    public Quiz postQuiz(@Valid @RequestBody Quiz quiz, Authentication auth) {
        UserDetailsImpl currentUser = (UserDetailsImpl) auth.getPrincipal();
        Long userId = currentUser.getId();
        User user = new User();
        user.setId(userId);
        quiz.setUser(user);
        
        return quizService.saveQuiz(quiz);
    }

    @PostMapping("/quizzes/{id}/solve")
    public AnswerCheck postAnswer(@PathVariable int id, @Valid  @RequestBody Answer answer, Authentication auth) {
        Quiz quiz = quizService.getQuizById(id);
        if (quiz.getAnswer() == null && answer.answer().length == 0 || Arrays.equals(quiz.getAnswer(), answer.answer())) {
            UserDetailsImpl currentUser = (UserDetailsImpl) auth.getPrincipal();
            Long userId = currentUser.getId();
            completedQuizRepo.save(
                    new CompletedQuiz( (long) id, userId, new Date() ) );
            return correctAnswer;
        } else {
            return wrongAnswer;
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
