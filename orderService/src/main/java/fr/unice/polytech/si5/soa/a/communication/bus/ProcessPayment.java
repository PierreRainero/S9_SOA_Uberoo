package fr.unice.polytech.si5.soa.a.communication.bus;

import fr.unice.polytech.si5.soa.a.communication.PaymentDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Class name	ProcessPayment
 * Date			21/10/2018
 * @author		PierreRainero
 */
@Data
@EqualsAndHashCode(callSuper=true)
@ToString()
public class ProcessPayment extends Message {
	private String account;
	private double amount;

	/**
	 * Default constructor
	 */
	public ProcessPayment() {
		// Default constructor for Jackson databinding
	}
	
	/**
	 * Normal constructor for an "PROCESS_PAYMENT" message
	 */
	/**
	 * Normal constructor for an "PROCESS_PAYMENT" message
	 * @param payment {@link PaymentDTO} to use to construct the message
	 */
	public ProcessPayment(PaymentDTO payment) {
		type = "PROCESS_PAYMENT";
		
		this.account = payment.getAccount();
		this.amount = payment.getAmount();
	}
}
