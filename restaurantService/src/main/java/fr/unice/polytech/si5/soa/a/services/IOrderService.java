package fr.unice.polytech.si5.soa.a.services;

import java.util.List;

import fr.unice.polytech.si5.soa.a.communication.RestaurantOrderDTO;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowOrderException;

/**
 * Class name	IOrderService
 * Date			08/10/2018
 * @author		PierreRainero
 */
public interface IOrderService {
	RestaurantOrderDTO addOrder(RestaurantOrderDTO orderToAdd);
	RestaurantOrderDTO updateOrder(RestaurantOrderDTO orderToUpdate) throws UnknowOrderException;
	List<RestaurantOrderDTO> getOrdersToDo();
}
