package fr.unice.polytech.si5.soa.a.dao;

import java.util.List;
import java.util.Optional;

import fr.unice.polytech.si5.soa.a.entities.Restaurant;
import fr.unice.polytech.si5.soa.a.entities.RestaurantOrder;

/**
 * Class name	IOrderDao
 * Date			08/10/2018
 * @author		PierreRainero
 * 
 * @version		1.1
 * Date			23/10/2018
 */
public interface IOrderDao {
	RestaurantOrder addOrder(RestaurantOrder orderToAdd);
	RestaurantOrder updateOrder(RestaurantOrder orderToUpdate);
	Optional<RestaurantOrder> findOrderById(int id);
	List<RestaurantOrder> getOrdersToDo(Restaurant restaurantConcerned);
}
