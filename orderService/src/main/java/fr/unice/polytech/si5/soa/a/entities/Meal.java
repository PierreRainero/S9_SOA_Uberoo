package fr.unice.polytech.si5.soa.a.entities;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.NONE;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.lang.NonNull;

import fr.unice.polytech.si5.soa.a.communication.MealDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

/**
 * Class name	Meal
 * Date			29/09/2018
 * @author		PierreRainero
 */
@Entity
@Data
@Table(name = "`MEAL`")
@EqualsAndHashCode(exclude={"id", "tags", "feedbacks"})
@ToString()
public class Meal implements Serializable {
	/**
	 * Generated UID version
	 */
	private static final long serialVersionUID = -6968907828470010887L;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id")
	@Setter(NONE)
	private int id;
	
	@Column(name = "name", nullable = false)
	private String name;
	
	@ElementCollection(fetch = FetchType.EAGER)
	private List<String> tags = new ArrayList<>();
	
	@ManyToOne(cascade = CascadeType.MERGE)
	@NonNull
	private Restaurant restaurant;
	
	@Column(name = "price", nullable = false)
	private double price;
	
	@Setter(NONE)
	@OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "meal")
	@ToString.Exclude
	private List<Feedback> feedbacks = new ArrayList<>();

	/**
	 * Default constructor
	 */
	public Meal() {
		// Default constructor for JPA
	}
	
	/**
	 * Normal construtor using Data Transfert Object
	 * @param mealDatas DTO for {@link Meal}
	 */
	public Meal(MealDTO mealDatas) {
		this.name = mealDatas.getName();
		this.tags = mealDatas.getTags();
	}
	
	/**
	 * Generate a Data Transfer Object from a business object
	 * @return DTO for a {@link Meal}
	 */
	public MealDTO toDTO() {
		return new MealDTO(name, tags, restaurant.toDTO(), price);
	}
	
	/**
	 * Add a tag to the tag list
	 * @param tag tag to add
	 */
	public void addTag(String tag) {
		if(!tags.contains(tag)) {
			tags.add(tag);
		}
	}
	
	/**
	 * Remove a tag to the tag list
	 * @param tag tag to remove
	 */
	public void removeTag(String tag) {
		tags.remove(tag);
	}
	
	/**
	 * Add a new feedback to the meal
	 * @param newFeedback feedback to add
	 */
	public void addFeedback(Feedback newFeedback) {
		feedbacks.add(newFeedback);
	}
	
	/**
	 * Remove a feedback to the meal
	 * @param feedbackToRemove feedback to remove
	 */
	public void removeFeedback(Feedback feedbackToRemove) {
		feedbacks.remove(feedbackToRemove);
	}
}
