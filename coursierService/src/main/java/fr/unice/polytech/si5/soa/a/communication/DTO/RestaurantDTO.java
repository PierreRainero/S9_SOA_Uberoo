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
    private String name;
    private String address;

    public RestaurantDTO() {
    }

    public RestaurantDTO(String restaurantName, String restaurantAddress) {
        this.name = restaurantName;
        this.address = restaurantAddress;
    }

    public RestaurantDTO(Integer id, Double longitude, Double latitude, String name, String address) {
        this(name, address);
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
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
        if (name != null) {
            restaurant.setName(name);
        }
        if (address != null) {
            restaurant.setAddress(address);
        }
        return restaurant;
    }
}
