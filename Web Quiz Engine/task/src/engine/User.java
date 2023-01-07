package engine;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Email(regexp = ".+@.+\\..+", message = "Email should be valid")
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    @Size(min = 5)
    private String password;

    @OneToMany(mappedBy = "user")
    private List<Quiz> quizzes;

}
