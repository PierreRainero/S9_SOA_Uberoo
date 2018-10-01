package fr.unice.polytech.si5.soa.a.entities;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "`Meal`")
@EqualsAndHashCode(exclude={"id"})
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
	}
	
	/**
	 * Generate a Data Transfer Object from a business object
	 * @return DTO for a {@link Meal}
	 */
	public MealDTO toDTO() {
		return new MealDTO(name);
	}
}
