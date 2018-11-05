package fr.unice.polytech.si5.soa.a.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.NonNull;

import fr.unice.polytech.si5.soa.a.communication.FeedbackDTO;

import javax.persistence.*;

import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.NONE;

@Entity
@Data
@Table(name = "`FEEDBACK`")
@EqualsAndHashCode(exclude={"id"})
@ToString()
public class Feedback implements Serializable {
    /**
	 * Generated UID version
	 */
	private static final long serialVersionUID = -3883515512791433672L;

	@Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    @Setter(NONE)
    private int id;
    
    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne(cascade = CascadeType.MERGE)
    @NonNull
    private Meal meal;

    public Feedback() {

    }

    public Feedback(FeedbackDTO datas) {
    	this.author = datas.getAuthor();
    	this.content = datas.getContent();
    }
    
    public FeedbackDTO toDTO() {
    	return new FeedbackDTO(author, content, meal.toDTO());
    }
}
