package fr.unice.polytech.si5.soa.a.services;

import fr.unice.polytech.si5.soa.a.communication.DTO.RestaurantDTO;

public interface IRestaurantService {
    /**
     * Add the following restaurant to the system.
     *
     * @param restaurantToAdd The restaurant to add.
     * @return The restaurant which has been saved in the system.
     */
    RestaurantDTO addRestaurant(RestaurantDTO restaurantToAdd);

    /**
     * Find the restaurant thanks to the parameters.
     *
     * @param name    Name of the restaurant.
     * @param address Address of the restaurant
     * @return The corresponding restaurant
     */
    RestaurantDTO findRestaurant(String name, String address);
}
