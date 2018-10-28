package fr.unice.polytech.si5.soa.a.entities;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import fr.unice.polytech.si5.soa.a.communication.DeliveryDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Class name	Delivery
 * Date			08/10/2018
 *
 * @author PierreRainero
 */
@Entity
@Data
@Table(name = "`DELIVERY`")
@EqualsAndHashCode(exclude = {"id"})
@ToString()
public class Delivery implements Serializable {
    /**
     * Generated UID version
     */
    private static final long serialVersionUID = 8262931988309989234L;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "coursier_id")
    private Integer coursierId;

    @Column(name = "restaurant_id")
    private Integer restaurantId;

    @Column(name = "deliveryAddress")
    private String deliveryAddress;

    @Column(name = "state")
    public Boolean state = false;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "coursierGetPaid")
    private Boolean coursierGetPaid = false;

    @Column(name = "creation_date")
    private Date creationDate;

    @Column(name = "delivery_date")
    private Date deliveryDate;

    public Delivery() {
        // Default constructor for JPA
    }

    public Delivery(DeliveryDTO data) {
        this.deliveryAddress = data.getDeliveryAddress();
        this.state = data.isState();
        this.restaurantId = data.getRestaurantId();
        this.coursierGetPaid = data.isCoursierGetPaid();
    }

    public DeliveryDTO toDTO() {
        DeliveryDTO deliveryDTO = new DeliveryDTO();
        deliveryDTO.setId(id);

        if (deliveryAddress != null) {
            deliveryDTO.setDeliveryAddress(deliveryAddress);
        }
        if (state != null) {
            deliveryDTO.setState(state);
        }
        if (latitude != null) {
            deliveryDTO.setLatitude(latitude);
        }
        if (longitude != null) {
            deliveryDTO.setLongitude(longitude);
        }
        if (restaurantId != null) {
            deliveryDTO.setRestaurantId(restaurantId);
        }
        if (coursierId != null) {
            deliveryDTO.setCoursierId(coursierId);
        }
        return deliveryDTO;
    }
}
