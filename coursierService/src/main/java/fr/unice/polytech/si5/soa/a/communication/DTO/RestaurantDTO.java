package fr.unice.polytech.si5.soa.a.communication.DTO;

import fr.unice.polytech.si5.soa.a.entities.Restaurant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@EqualsAndHashCode(exclude = {"id"})
@ToString()
public class RestaurantDTO implements Serializable {
    private Integer id;
    private Double latitude;
    private Double longitude;

    public RestaurantDTO() {
    }

    public RestaurantDTO(Integer id) {
        this.id = id;
    }

    public RestaurantDTO(Integer id, Double latitude, Double longitude) {
        this(id);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Restaurant createRestaurant() {
        Restaurant restaurant = new Restaurant();
        if (id != null) {
            restaurant.setId(id);
        }
        if (latitude != null) {
            restaurant.setLatitude(latitude);
        }
        if (longitude != null) {
            restaurant.setLongitude(longitude);
        }
        return restaurant;
    }
}
