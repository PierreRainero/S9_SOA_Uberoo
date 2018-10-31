package fr.unice.polytech.si5.soa.a.communication;

import fr.unice.polytech.si5.soa.a.entities.Restaurant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@EqualsAndHashCode(exclude = {"id"})
@ToString()
public class RestaurantDto implements Serializable {
    private Integer id;
    private Double latitude;
    private Double longitude;

    public RestaurantDto() {
    }

    public RestaurantDto(Integer id) {
        this.id = id;
    }

    public RestaurantDto(Integer id, Double latitude, Double longitude) {
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
