package fr.unice.polytech.si5.soa.a.entities;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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
@EqualsAndHashCode(exclude = {"id", "coursier", "food"})
@ToString()
public class Delivery implements Serializable {
    /**
     * Generated UID version
     */
    private static final long serialVersionUID = 8262931988309989234L;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Integer id;

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

    @Fetch(FetchMode.SELECT)
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> food = new ArrayList<>();

    public Delivery() {
        // Default constructor for JPA
    }

    public Delivery(DeliveryDTO data) {
        this.deliveryAddress = data.getDeliveryAddress();
        this.food = data.getFood();
        creationDate = data.getCreationDate();
        this.latitude = data.getLatitude();
        this.longitude = data.getLongitude();
    }

    public DeliveryDTO toDTO() {
        DeliveryDTO deliveryDTO = new DeliveryDTO();
        if (id != null) {
            deliveryDTO.setId(id);
        }
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
        if (!food.isEmpty()) {
            deliveryDTO.setFood(food);
        }
        deliveryDTO.setCreationDate(creationDate);
        return deliveryDTO;
    }
}
