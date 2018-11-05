package fr.unice.polytech.si5.soa.a.services;

import fr.unice.polytech.si5.soa.a.communication.DTO.RestaurantDTO;

public interface IRestaurantService {
	public RestaurantDTO addRestaurant(RestaurantDTO restaurantToAdd);
	public RestaurantDTO findRestaurant(String name, String address);
}
