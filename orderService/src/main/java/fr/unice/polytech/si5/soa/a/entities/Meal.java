package fr.unice.polytech.si5.soa.a.entities;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import fr.unice.polytech.si5.soa.a.communication.MealDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Class name	Meal
 * Date			29/09/2018
 * @author		PierreRainero
 */
@Entity
@Data
@Table(name = "`MEAL`")
@EqualsAndHashCode(exclude={"id", "tags"})
@ToString()
public class Meal implements Serializable {
	/**
	 * Generated UID version
	 */
	private static final long serialVersionUID = -6968907828470010887L;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "name", nullable = false, unique=true)
	private String name;
	
	@ElementCollection(fetch = FetchType.EAGER)
	private List<String> tags = new ArrayList<>();

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
	public Meal(MealDTO mealDatas, List<String> tags) {
		this.name = mealDatas.getName();
		this.tags = mealDatas.getTags();
	}
	
	/**
	 * Generate a Data Transfer Object from a business object
	 * @return DTO for a {@link Meal}
	 */
	public MealDTO toDTO() {
		return new MealDTO(name, tags);
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
}
