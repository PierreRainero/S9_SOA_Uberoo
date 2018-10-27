package fr.unice.polytech.si5.soa.a.communication;

/**
 * Class OrderDelivered
 *
 * @author Joël CANCELA VAZ
 */
public class OrderDelivered extends Message {
	public String address;

	public OrderDelivered() {
		type = "ORDER_DELIVERED";
	}
}
