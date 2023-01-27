package engine.services;

import engine.entities.CompletedQuiz;
import engine.entities.Quiz;
import engine.exceptions.ForbiddenException;
import engine.exceptions.NotFoundException;
import engine.repos.CompletedQuizRepository;
import engine.repos.QuizRepository;
import engine.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

@Service
public class QuizService {
    private final QuizRepository quizRepository;

    private final CompletedQuizRepository completedQuizRepo;

    public record AnswerCheck(boolean success, String feedback) {}
    public record Answer(int[] answer) {}
    AnswerCheck correctAnswer = new AnswerCheck(true, "Congratulations, you are right!");
    AnswerCheck wrongAnswer = new AnswerCheck(false, "Wrong answer! Please try again.");

    @Autowired
    public QuizService(QuizRepository quizRepository, CompletedQuizRepository completedQuizRepo) {
        this.quizRepository = quizRepository;
        this.completedQuizRepo = completedQuizRepo;
    }

    public Quiz getQuizById(long id) {
        return quizRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public Page<Quiz> findAllQuizzes(PageRequest request) {
        return quizRepository.findAll(request);
    }

    public Quiz saveQuiz(Quiz quizToSave) {
        return quizRepository.save(quizToSave);
    }
    

    public ResponseEntity<Object> deleteQuiz(int id) {
        Long userId = UserDetailsImpl.getCurrentUserID();
        Quiz quiz = getQuizById(id);
        if (Objects.equals(quiz.getUser().getId(), userId)) {
            quizRepository.deleteById((long) id);
            return ResponseEntity.status(204).build();
        } else {
            throw new ForbiddenException();
        }
    }

    public AnswerCheck checkAnswer(Answer answer, int id) {
        Quiz quiz = getQuizById(id);
        if (quiz.getAnswer() == null && answer.answer().length == 0 || Arrays.equals(quiz.getAnswer(), answer.answer())) {
            Long userId = UserDetailsImpl.getCurrentUserID();
            completedQuizRepo.save(
                    new CompletedQuiz( (long) id, userId, new Date() ) );
            return correctAnswer;
        } else {
            return wrongAnswer;
        }
    }
}

