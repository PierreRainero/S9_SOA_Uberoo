package fr.unice.polytech.si5.soa.a.exceptions;

import java.io.Serializable;

public class UnknowRestaurantException extends Exception implements Serializable {
	public UnknowRestaurantException(String message) {
		super(message);
	}
}
