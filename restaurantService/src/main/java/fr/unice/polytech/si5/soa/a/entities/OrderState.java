package fr.unice.polytech.si5.soa.a.entities;

/**
 * Class name	OrderState
 * Date			08/10/2018
 * @author		PierreRainero
 */
public enum OrderState {
	TO_PREPARE("TO PREPARE"),
	FINISHED("FINISHED"),
	DELIVERED("DELIVERED");
	
	private String details;
	
	OrderState(String value) {
		this.details = value;
	}
	
	public String getDetails() {
		return details;
	}
}
