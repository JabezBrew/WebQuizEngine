package engine.services;

import engine.entities.Quiz;
import engine.exceptions.NotFoundException;
import engine.repos.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class QuizService {
    private final QuizRepository quizRepository;

    @Autowired
    public QuizService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
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

    public void deleteAllQuizzes() {
        quizRepository.deleteAll();
    }

    public void deleteQuiz(int id) {
        quizRepository.deleteById((long) id);
    }
}

