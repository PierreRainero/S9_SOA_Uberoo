package fr.unice.polytech.si5.soa.a.communication;

import java.io.Serializable;
import java.util.Date;

import fr.unice.polytech.si5.soa.a.entities.Coursier;
import fr.unice.polytech.si5.soa.a.entities.Delivery;
import fr.unice.polytech.si5.soa.a.entities.Restaurant;
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
@EqualsAndHashCode(exclude = {"id", "coursier", "restaurant"})
@ToString()
public class DeliveryDTO implements Serializable {
    /**
     * Generated UID version
     */
    private static final long serialVersionUID = 2206465850241015470L;
    private RestaurantDto restaurant;
    int id;
    private String deliveryAddress;
    private Double latitude;
    private Double longitude;
    private Date creationDate;
    private Date deliveryDate;
    public boolean state = false;
    private boolean coursierGetPaid = false;
    private CoursierDto coursier;

    public DeliveryDTO() {
        // Default constructor for Jackson databinding
    }

    public DeliveryDTO(int id, String deliveryAddress, boolean state, RestaurantDto restaurant) {
        this.id = id;
        this.deliveryAddress = deliveryAddress;
        this.state = state;
        this.restaurant = restaurant;
    }

    public DeliveryDTO(int id, String deliveryAddress, boolean state, Double latitude, Double longitude, RestaurantDto restaurant, CoursierDto coursier) {
        this(id, deliveryAddress, state, restaurant);
        this.latitude = latitude;
        this.longitude = longitude;
        this.coursier = coursier;
    }
}
