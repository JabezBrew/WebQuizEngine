package engine.repos;

import engine.entities.CompletedQuiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompletedQuizRepository extends PagingAndSortingRepository<CompletedQuiz, Long> {
    @Query("SELECT c FROM CompletedQuiz c WHERE c.userId = ?1")
    Page<CompletedQuiz> findQuizzesCompletedByUser(Long id, Pageable pageable);
    @Query("SELECT COUNT(c) FROM CompletedQuiz c WHERE c.userId = ?1")
    long countByUserId(Long userId);

}
