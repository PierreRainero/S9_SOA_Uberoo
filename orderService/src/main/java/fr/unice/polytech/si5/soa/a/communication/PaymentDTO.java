package fr.unice.polytech.si5.soa.a.communication;

import java.io.Serializable;

import fr.unice.polytech.si5.soa.a.entities.states.PaymentState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Class name	PaymentDTO
 * Date			21/10/2018
 * @author 		PierreRainero
 *
 */
@Data
@EqualsAndHashCode()
@ToString()
public class PaymentDTO implements Serializable {
	/**
	 * Generated UID version
	 */
	private static final long serialVersionUID = 7763567211737500525L;

	private int id;
	private String account;
	private double amount;
	private PaymentState state;
	
	/**
	 * Default constructor
	 */
	public PaymentDTO() {
		// Default constructor for Jackson databinding
	}
	
	/**
	 * Normal constructor
	 * @param id identifier of the payment
	 * @param account account used to receive the amount
	 * @param amount amount to send
	 * @param state status of the transaction
	 */
	public PaymentDTO(int id, String account, double amount, PaymentState state) {
		this.id = id;
		this.account = account;
		this.amount = amount;
		this.state = state;
	}
}
