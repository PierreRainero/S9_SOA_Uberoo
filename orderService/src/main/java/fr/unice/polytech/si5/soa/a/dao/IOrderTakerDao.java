package fr.unice.polytech.si5.soa.a.dao;

import java.util.Optional;

import fr.unice.polytech.si5.soa.a.entities.Order;

/**
 * Class name	IOrderTaker
 * Date			29/09/2018
 * @author		PierreRainero
 *
 */
public interface IOrderTakerDao {
	/**
	 * Add an {@link Order} into the database
	 * @param orderToAdd order to add
	 * @return the saved order
	 */
	Order addOrder(Order orderToAdd);
	
	/**
	 * Update an {@link Order} into the database
	 * @param orderToUpdate order to update
	 * @return the updated order
	 */
	Order updateOrder(Order orderToUpdate);
	
	/**
	 * Search an order in the database using his id
	 * @param orderId id to search
	 * @return the order wrapped in an {@link Optional} if the order exists, Optional.empty() otherwise
	 */
	Optional<Order> findOrderById(int orderId);
}
