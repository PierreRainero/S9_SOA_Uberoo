package fr.unice.polytech.si5.soa.a.communication;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Class name	DeliveryDTO
 * Date			08/10/2018
 * @author		PierreRainero
 */
@Data
@EqualsAndHashCode(exclude={"id"})
@ToString()
public class DeliveryDTO implements Serializable {
	/**
	 * Generated UID version
	 */
	private static final long serialVersionUID = 2206465850241015470L;
	
	int id;
	private String deliveryAddress;
	public boolean state = false;
	
	public DeliveryDTO() {
		// Default constructor for Jackson databinding
	}
	
	public DeliveryDTO(int id, String deliveryAddress, boolean state) {
		this.id = id;
		this.deliveryAddress = deliveryAddress;
		this.state = state;
	}
}
