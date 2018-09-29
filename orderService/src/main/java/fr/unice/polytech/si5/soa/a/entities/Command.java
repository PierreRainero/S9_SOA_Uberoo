package fr.unice.polytech.si5.soa.a.entities;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.NONE;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

@Entity
@Data
@Table(name = "`COMMAND`")
@EqualsAndHashCode(exclude={"id"})
@ToString()
public class Command implements Serializable {
	/**
	 * Generated UID version
	 */
	private static final long serialVersionUID = 6853129339978021134L;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id")
	@Setter(NONE)
	private int id;
	
	@ManyToMany
	@Setter(NONE)
	private List<Meal> meals = new ArrayList<>();

	public Command() {
		// Default constructor for JPA
	}
	
	public void addMeal(Meal meal) {
		meals.add(meal);
	}
	
	public void removeMeal(Meal meal) {
		meals.remove(meal);
	}
}
