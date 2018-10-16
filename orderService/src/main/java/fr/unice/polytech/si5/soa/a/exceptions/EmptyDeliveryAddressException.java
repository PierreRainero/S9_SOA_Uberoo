package fr.unice.polytech.si5.soa.a.exceptions;

import java.io.Serializable;

/**
 * Class name	EmptyDeliveryAddressException
 * Date			01/10/2018
 * @author		PierreRainero
 */
public class EmptyDeliveryAddressException extends Exception implements Serializable {
	/**
	 * Generated UID version
	 */
	private static final long serialVersionUID = -6802285949011562039L;

	public EmptyDeliveryAddressException(String message) {
		super(message);
	}
}
