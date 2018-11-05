package fr.unice.polytech.si5.soa.a.communication.message;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Class OrderDelivered
 *
 * @author Joël CANCELA VAZ
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString()
public class OrderDelivered extends Message {
	public String address;
	public static String messageType = "ORDER_DELIVERED";
	private Date date;
	private Integer deliveryId;
	private String account;
	private double amount;
	private String restaurantName;
	private String restaurantAddress;
	private String deliveryAddress;
	private List<String> food = new ArrayList<>();

	public OrderDelivered() {
		type = messageType;
	}
}
