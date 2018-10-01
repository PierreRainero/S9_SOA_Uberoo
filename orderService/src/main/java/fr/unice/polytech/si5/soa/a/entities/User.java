package fr.unice.polytech.si5.soa.a.entities;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.NONE;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import fr.unice.polytech.si5.soa.a.communication.UserDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

/**
 * Class name	User
 * Date			30/09/2018
 * @author		PierreRainero
 */
@Entity
@Data
@Table(name = "`USER`")
@EqualsAndHashCode(exclude={"id"})
@ToString()
public class User implements Serializable {
	/**
	 * Generated UID version
	 */
	private static final long serialVersionUID = 1356070369380001777L;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id")
	@Setter(NONE)
	private int id;
	
	@Setter(NONE)
	@OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "transmitter")
	@ToString.Exclude
	private List<Order> commands = new ArrayList<>();
	
	@Column(name = "lastName", nullable = false)
	private String lastName;
	
	@Column(name = "firstName", nullable = false)
	private String firstName;
	
	/**
	 * Default constructor
	 */
	public User() {
		// Default constructor for JPA
	}

	/**
	 * Normal construtor using Data Transfert Object
	 * @param userDatas DTO for {@link User}
	 */
	public User(UserDTO userDatas) {
		this.lastName = userDatas.getLastName();
		this.firstName = userDatas.getFirstName();
	}
	
	/**
	 * Generate a Data Transfer Object from a business object
	 * @return DTO for a {@link User}
	 */
	public UserDTO toDTO() {
		return new UserDTO(id, lastName, firstName);
	}
}
