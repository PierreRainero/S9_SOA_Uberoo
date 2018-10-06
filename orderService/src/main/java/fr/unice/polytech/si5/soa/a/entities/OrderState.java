package fr.unice.polytech.si5.soa.a.entities;

/**
 * Class name	OrderState
 * Date			04/10/2018
 * @author		PierreRainero
 */
public enum OrderState {
	WAITING("WAITING"),
	VALIDATED("VALIDATED"),
	REFUSED("REFUSED");
	
	private String details;
	
	OrderState(String value) {
		this.details = value;
	}
	
	public String getDetails() {
		return details;
	}
}