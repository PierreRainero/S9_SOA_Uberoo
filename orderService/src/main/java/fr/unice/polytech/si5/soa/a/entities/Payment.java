package fr.unice.polytech.si5.soa.a.entities;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.NONE;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import fr.unice.polytech.si5.soa.a.communication.PaymentDTO;
import fr.unice.polytech.si5.soa.a.entities.states.PaymentState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

/**
 * Class name	Payment
 * Date			22/10/2018
 * @author 		PierreRainero
 *
 */
@Entity
@Data
@Table(name = "`PAYMENT`")
@EqualsAndHashCode(exclude={"id"})
public class Payment implements Serializable {
	/**
	 * Generated UID version
	 */
	private static final long serialVersionUID = 738371814341856035L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id")
	@Setter(NONE)
	private int id;
	
	@Column(name = "account", nullable = false)
	private String account;
	
	@Column(name = "amount", nullable = false)
	private double amount;
	
	@ManyToOne(cascade = CascadeType.MERGE)
	private UberooOrder order;
	
	@Enumerated(EnumType.STRING)
	private PaymentState state = PaymentState.SENT;
	
	/**
	 * Default constructor
	 */
	public Payment() {
		// Default constructor for JPA
	}
	
	/**
	 * Normal constructor
	 * @param paymentDatas DTO for {@link Payment}
	 */
	public Payment(PaymentDTO paymentDatas) {
		this.account = paymentDatas.getAccount();
		this.amount = paymentDatas.getAmount();
		// We only allow the bank to change the state
	}
	
	/**
	 * Generate a Data Transfer Object from a business object
	 * @return DTO for a {@link Payment}
	 */
	public PaymentDTO toDTO() {
		return new PaymentDTO(id, account, amount, state);
	}
}
