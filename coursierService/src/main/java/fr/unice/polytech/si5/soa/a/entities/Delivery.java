package fr.unice.polytech.si5.soa.a.entities;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import fr.unice.polytech.si5.soa.a.communication.DTO.DeliveryDTO;
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
@EqualsAndHashCode(exclude = {"id", "coursier"})
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

    @ManyToOne(cascade = CascadeType.MERGE)
    private Coursier coursier;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Restaurant restaurant;

    @Column(name = "deliveryAddress")
    private String deliveryAddress;

    @Column(name = "state")
    public Boolean state = false;

    @Column(name = "cancel")
    public Boolean cancel = false;

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
        this.restaurant = data.getRestaurant().createRestaurant();
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
        if (restaurant != null) {
            deliveryDTO.setRestaurant(restaurant.toDto());
        }
        if (coursier != null) {
            deliveryDTO.setCoursier(coursier.toDto());
        }
        if (cancel != null) {
            deliveryDTO.setCancel(cancel);
        }
        return deliveryDTO;
    }
}
