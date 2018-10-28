package fr.unice.polytech.si5.soa.a.communication;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Class name	DeliveryDTO
 * Date			08/10/2018
 *
 * @author PierreRainero
 */
@Data
@EqualsAndHashCode(exclude = {"id"})
@ToString()
public class DeliveryDTO implements Serializable {
    /**
     * Generated UID version
     */
    private static final long serialVersionUID = 2206465850241015470L;
    private Integer restaurantId;
    int id;
    private String deliveryAddress;
    private Double latitude;
    private Double longitude;
    private Date creationDate;
    private Date deliveryDate;
    public boolean state = false;
    private boolean coursierGetPaid = false;
    private Integer coursierId;

    public DeliveryDTO() {
        // Default constructor for Jackson databinding
    }

    public DeliveryDTO(int id, String deliveryAddress, boolean state, Integer restaurantId) {
        this.id = id;
        this.deliveryAddress = deliveryAddress;
        this.state = state;
        this.restaurantId = restaurantId;
    }

    public DeliveryDTO(int id, String deliveryAddress, boolean state, Double latitude, Double longitude, Integer restaurantId, Integer coursierId) {
        this(id, deliveryAddress, state, restaurantId);
        this.latitude = latitude;
        this.longitude = longitude;
        this.coursierId = coursierId;
    }
}
