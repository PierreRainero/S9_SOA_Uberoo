package fr.unice.polytech.si5.soa.a.communication.DTO;

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
@EqualsAndHashCode(exclude = {"id", "coursier", "restaurant"})
@ToString()
public class DeliveryDTO implements Serializable {
    /**
     * Generated UID version
     */
    private static final long serialVersionUID = 2206465850241015470L;
    private RestaurantDTO restaurant;
    int id;
    private String deliveryAddress;
    private Double latitude;
    private Double longitude;
    private Date creationDate;
    private Date deliveryDate;
    public boolean state = false;
    private boolean coursierGetPaid = false;
    private CoursierDTO coursier;
    public Boolean cancel = false;

    public DeliveryDTO() {
        // Default constructor for Jackson databinding
    }

    public DeliveryDTO(int id, String deliveryAddress, boolean state, RestaurantDTO restaurant) {
        this.id = id;
        this.deliveryAddress = deliveryAddress;
        this.state = state;
        this.restaurant = restaurant;
    }

    public DeliveryDTO(int id, String deliveryAddress, boolean state, Double latitude, Double longitude, RestaurantDTO restaurant, CoursierDTO coursier) {
        this(id, deliveryAddress, state, restaurant);
        this.latitude = latitude;
        this.longitude = longitude;
        this.coursier = coursier;
    }
}
