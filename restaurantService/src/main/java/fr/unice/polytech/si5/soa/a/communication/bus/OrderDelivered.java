package fr.unice.polytech.si5.soa.a.communication.bus;

/**
 * Class OrderDelivered
 *
 * @author Joël CANCELA VAZ
 */
public class OrderDelivered extends Message {
	public static String messageType = "ORDER_DELIVERED";
	private String address;

	public OrderDelivered() {
		type = messageType;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}

