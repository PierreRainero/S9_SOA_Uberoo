package fr.unice.polytech.si5.soa.a.restaurantservice.model;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.NONE;

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


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

@Entity
@Data
@Table(name = "`ORDERTOPREPARE`")
@EqualsAndHashCode(exclude={"id"})
@ToString()
public class OrderToPrepare implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -4603988534527511525L;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id")
	@Setter(NONE)
	private int id;
	
	@ElementCollection(fetch = FetchType.EAGER)
	private List<String> meals = new ArrayList<>();

	/**
	 * Default constructor
	 */
	public OrderToPrepare() {
		// Default constructor for JPA
	}
	
	public void addMeal(String meal) {
		meals.add(meal);
	}
	
	public void removeMeal(String meal) {
		meals.remove(meal);
	}
}
