package engine.repos;

import engine.entities.Quiz;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface QuizRepository extends PagingAndSortingRepository<Quiz, Long> {
}

