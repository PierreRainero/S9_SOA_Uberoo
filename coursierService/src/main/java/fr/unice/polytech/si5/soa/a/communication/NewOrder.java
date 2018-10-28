package fr.unice.polytech.si5.soa.a.communication;

import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Class name	NewOrder
 * Date			06/10/2018
 *
 * @author PierreRainero
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString()
public class NewOrder extends Message {
    public String address;
    public List<String> food;
    public Integer id;

    /**
     * Default constructor
     */
    public NewOrder() {
        // Default constructor for Jackson databinding
    }

    public DeliveryDTO createDelivery() {
        return new DeliveryDTO(id, address, false);
    }
}
