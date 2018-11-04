package fr.unice.polytech.si5.soa.a.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.unice.polytech.si5.soa.a.communication.RestaurantOrderDTO;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowMealException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowOrderException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowRestaurantException;

/**
 * Class name	IOrderService
 * Date			08/10/2018
 * @author		PierreRainero
 */
public interface IOrderService {
	RestaurantOrderDTO addOrder(RestaurantOrderDTO orderToAdd) throws UnknowRestaurantException, UnknowMealException;
	RestaurantOrderDTO addOrder(RestaurantOrderDTO orderToAdd, List<String> meals, String restaurantName, String restaurantAddress) throws UnknowRestaurantException, UnknowMealException;
	
	RestaurantOrderDTO updateOrder(RestaurantOrderDTO orderToUpdate) throws UnknowOrderException;
	RestaurantOrderDTO deliverOrder(String restaurantName, String restaurantAddres, String deliveryAddress, List<String> meals, Date validationDate) throws UnknowOrderException, UnknowRestaurantException, UnknowMealException;
	
	List<RestaurantOrderDTO> getOrdersToDo(int restaurantId) throws UnknowRestaurantException;
}
