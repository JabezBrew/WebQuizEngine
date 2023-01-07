package engine;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;


@JsonPropertyOrder({"id", "title", "text", "options", "answer"})
@Entity
@Table(name = "quiz")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Quiz {
    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    @NotEmpty
    @JsonProperty
    @Column
    private String title;
    
    @NotEmpty
    @JsonProperty
    @Column
    private String text;
    
    @NotNull
    @Size(min=2, message = "options should contain at least two possible answers")
    @JsonProperty
    @Column
    private String[] options;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) //means answer will not be deserialized from json to a field in the class.
    @Column                                                 // contd: it's serialized when it's being written to json though hence it's availability as compared to ignore.
    private int[] answer;

    @ManyToOne
    @JoinColumn(name="userId")
    @JsonIgnore
    private User user;
}

