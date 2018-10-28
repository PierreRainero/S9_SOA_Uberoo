package fr.unice.polytech.si5.soa.a.entities;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

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

    @Column(name = "deliveryAddress", nullable = false)
    private String deliveryAddress;

    @Column(name = "state", nullable = false)
    public boolean state = false;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "coursierGetPaid")
    private Boolean coursierGetPaid;

    public Delivery() {
        // Default constructor for JPA
    }

    public Delivery(DeliveryDTO data) {
        this.deliveryAddress = data.getDeliveryAddress();
        this.state = data.isState();
    }

    public DeliveryDTO toDTO() {
        return new DeliveryDTO(id, deliveryAddress, state, latitude, longitude);
    }
}
