package fr.unice.polytech.si5.soa.a.services;

import fr.unice.polytech.si5.soa.a.communication.RestaurantDTO;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowRestaurantException;

public interface IRestaurantService {
	RestaurantDTO addRestaurant(RestaurantDTO restaurant);
	RestaurantDTO findRestaurant(String name, String address) throws UnknowRestaurantException;
	RestaurantDTO findRestaurantById(int id) throws UnknowRestaurantException;
}
