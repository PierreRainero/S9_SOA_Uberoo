package fr.unice.polytech.si5.soa.a.communication.DTO;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Class name	DeliveryDTO
 * Date			08/10/2018
 *
 * @author 		PierreRainero
 */
@Data
@EqualsAndHashCode(exclude = {"id", "coursier", "restaurant", "food"})
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
    private List<String> food;

    public DeliveryDTO() {
        // Default constructor for Jackson databinding
    }

    public DeliveryDTO(String address, List<String> food, RestaurantDTO restaurant, Date creationDate) {
        this.deliveryAddress = address;
        this.food = food;
        this.restaurant = restaurant;
        this.latitude = 9.7;
        this.longitude = 9.7;
        this.creationDate = creationDate;
    }
}
