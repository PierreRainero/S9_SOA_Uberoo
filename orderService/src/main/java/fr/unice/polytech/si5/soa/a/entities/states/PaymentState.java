package fr.unice.polytech.si5.soa.a.entities.states;

/**
 * Class name	PaymentState
 * Date			22/10/2018
 * @author		PierreRainero
 */
public enum PaymentState {
	SENT("SENT"),
	ACCEPTED("ACCEPTED"),
	REFUSED("REFUSED");
	
	private String operation;
	
	PaymentState(String value) {
		this.operation = value;
	}
	
	public String getOperation() {
		return operation;
	}
}
