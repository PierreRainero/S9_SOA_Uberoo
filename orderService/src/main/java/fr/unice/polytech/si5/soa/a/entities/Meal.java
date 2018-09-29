package fr.unice.polytech.si5.soa.a.entities;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

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
	
	@Column(name = "name", nullable = false)
	protected String name;

	public Meal() {
		// Default constructor for JPA
	}
}
