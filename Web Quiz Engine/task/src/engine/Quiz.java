package engine;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.persistence.*;
import javax.validation.constraints.*;


@JsonPropertyOrder({"id", "title", "text", "options", "answer"})
@Entity
@Table(name = "quiz")
public class Quiz {
    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long generatedId;
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

    public Quiz() {}

    public Quiz(String title, String text, String[] options, int[] answer) {
        long id = generatedId;
        this.title = title;
        this.text = text;
        this.options = options;
        this.answer = answer;
    }


    public long id() {
        return generatedId;
    }

    public String title() {
        return title;
    }

    public String text() {
        return text;
    }

    public String[] options() {
        return options;
    }

    public int[] answer() {
        return answer;
    }

}

