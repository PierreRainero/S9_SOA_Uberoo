package fr.unice.polytech.si5.soa.a.exceptions;

import java.io.Serializable;

/**
 * Class name	UnknowRestaurantException
 * Date			21/10/2018
 * @author		PierreRainero
 */
public class UnknowRestaurantException extends Exception implements Serializable {
	/**
	 * Generated UID version
	 */
	private static final long serialVersionUID = 4770449604527242246L;

	public UnknowRestaurantException(String message) {
		super(message);
	}
}
