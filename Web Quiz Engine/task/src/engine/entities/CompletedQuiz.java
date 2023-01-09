package engine.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "completed_quizzes")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompletedQuiz {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long completedId;

    @Column(name = "quiz_id", nullable = false)
    private Long id;

    @Column(name = "user_id", nullable = false)
    @JsonIgnore
    private Long userId;

    @Column(name = "completedAt", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date completedAt;

    public CompletedQuiz(Long id, Long userId, Date completedAt) {
        this.completedId = completedId; //because id needed a default value
        this.id = id;
        this.userId = userId;
        this.completedAt = completedAt;
    }

}
