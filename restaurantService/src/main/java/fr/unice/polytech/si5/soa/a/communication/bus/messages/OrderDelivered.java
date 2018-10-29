package fr.unice.polytech.si5.soa.a.communication.bus.messages;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Class OrderDelivered
 *
 * @author Joël CANCELA VAZ
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString()
public class OrderDelivered extends Message {
	public static String messageType = "ORDER_DELIVERED";
	
	private String restaurantName;
	private String restaurantAddress;
	private String deliveryAddress;
	private List<String> food = new ArrayList<>();
	private Date date;

	public OrderDelivered() {
		type = messageType;
	}
}

