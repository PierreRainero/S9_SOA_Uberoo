package fr.unice.polytech.si5.soa.a.exceptions;

import java.io.Serializable;

public class UnknowDeliveryException extends Exception implements Serializable {
	/**
	 * Generated UID version
	 */
	private static final long serialVersionUID = -4424383533715188943L;

	public UnknowDeliveryException(String message) {
		super(message);
	}
}
