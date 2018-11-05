package fr.unice.polytech.si5.soa.a.communication.bus.messages;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Class name	ProcessPayment
 * Date			05/11/2018
 *
 * @author 		PierreRainero
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString()
public class ProcessPayment extends Message {
	private int id;
	private String account;
	private double amount;

	/**
	 * Default constructor
	 */
	public ProcessPayment() {
		// Default constructor for Jackson databinding
	}

	public ProcessPayment(String account, double amount) {
		type = "PROCESS_PAYMENT";

		this.id = -1;
		this.account = account;
		this.amount = amount;
	}
}
