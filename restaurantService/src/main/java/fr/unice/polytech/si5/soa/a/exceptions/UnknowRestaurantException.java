package fr.unice.polytech.si5.soa.a.exceptions;

import java.io.Serializable;

/**
 * Class name	UnknowRestaurantException
 * Date			22/10/2018
 * @author		PierreRainero
 */
public class UnknowRestaurantException extends Exception implements Serializable {
	/**
	 * Generated UID version
	 */
	private static final long serialVersionUID = -3387715981847504399L;

	public UnknowRestaurantException(String message) {
		super(message);
	}
}
