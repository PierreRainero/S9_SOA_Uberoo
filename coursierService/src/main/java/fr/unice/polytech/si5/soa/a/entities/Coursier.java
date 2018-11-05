package fr.unice.polytech.si5.soa.a.entities;

import fr.unice.polytech.si5.soa.a.communication.DTO.CoursierDTO;
import fr.unice.polytech.si5.soa.a.exceptions.UnknownDeliveryException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.NONE;

/**
 * Class Coursier
 *
 * @author Joël CANCELA VAZ
 */
@Entity
@Data
@Table(name = "`COURSIER`")
@EqualsAndHashCode(exclude = {"id", "deliveries"})
@ToString()
public class Coursier {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "account_number", nullable = false)
    private String accountNumber;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Setter(NONE)
    @OneToMany(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE}, fetch = FetchType.LAZY, mappedBy = "coursier")
    @ToString.Exclude
    private List<Delivery> deliveries = new ArrayList<>();

    public Coursier() {
    }
    
    public Coursier(CoursierDTO data) {
    	name = data.getName();
    	accountNumber = data.getAccountNumber();
    }

    public CoursierDTO toDto() {
        CoursierDTO coursierDTO = new CoursierDTO();
        if (id != null) {
            coursierDTO.setId(id);
        }
        if (name != null) {
            coursierDTO.setName(name);
        }
        if (latitude != null) {
            coursierDTO.setLatitude(latitude);
        }
        if (longitude != null) {
            coursierDTO.setLongitude(longitude);
        }
        return coursierDTO;
    }

    public void addDelivery(Delivery delivery) {
        this.deliveries.add(delivery);
    }

    public void updateDelivery(Delivery updateDelivery) throws UnknownDeliveryException {
        Optional<Delivery> sameDelivery = this.deliveries.stream()
                .filter(delivery -> delivery.getId() == updateDelivery.getId())
                .findFirst();
        if (!sameDelivery.isPresent()) {
            throw new UnknownDeliveryException(updateDelivery.getId());
        }
        sameDelivery.get().setState(updateDelivery.state);
    }

    public void removeDelivery(Delivery delivery) {
        this.deliveries.remove(delivery);
    }
}
