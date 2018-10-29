package fr.unice.polytech.si5.soa.a.entities;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.NONE;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.lang.NonNull;

import fr.unice.polytech.si5.soa.a.communication.FeedbackDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

/**
 * Class name	Meal
 * Date			29/10/2018
 * @author 		PierreRainero
 *
 */
@Entity
@Data
@Table(name = "`FEEDBACK`")
@EqualsAndHashCode(exclude={"id"})
@ToString()
public class Feedback implements Serializable {
    /**
	 * Generated UID version
	 */
	private static final long serialVersionUID = 6694116075315254376L;

	@Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    @Setter(NONE)
    private int id;
    
	@ManyToOne(cascade = CascadeType.MERGE)
    @NonNull
    private User author;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne(cascade = CascadeType.MERGE)
    @NonNull
    private Meal meal;
    
    /**
     * Default constructor
     */
    public Feedback() {
    	// Default constructor for JPA
    }
    
    /**
     * Normal construtor using Data Transfert Object
     * @param feedbackData DTO for {@link Feedback}
     */
    public Feedback(FeedbackDTO feedbackData) {
    	content = feedbackData.getContent();
    }
    
    /**
	 * Generate a Data Transfer Object from a business object
	 * @return DTO for a {@link Feedback}
	 */
    public FeedbackDTO toDTO() {
    	return new FeedbackDTO(author.toDTO(), content);
    }
}
