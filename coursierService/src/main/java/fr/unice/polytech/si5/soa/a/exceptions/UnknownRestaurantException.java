package fr.unice.polytech.si5.soa.a.exceptions;

public class UnknownRestaurantException extends Throwable {
    public UnknownRestaurantException(Integer restaurantId) {
        super("Can't find the coursier with id : " + restaurantId);
    }
    
    public UnknownRestaurantException(String message) {
    	super(message);
    }
}
