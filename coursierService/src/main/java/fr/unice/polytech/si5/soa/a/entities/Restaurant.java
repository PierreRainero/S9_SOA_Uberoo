package fr.unice.polytech.si5.soa.a.entities;

import fr.unice.polytech.si5.soa.a.communication.DTO.RestaurantDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Data
@Table(name = "`RESTAURANT`")
@EqualsAndHashCode(exclude = {"id"})
@ToString()
public class Restaurant implements Serializable {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "latitude")
    private Double latitude;

    public Restaurant() {
    }

    public RestaurantDTO toDto() {
        return new RestaurantDTO(id, longitude, latitude);
    }
}
