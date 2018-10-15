package fr.unice.polytech.si5.soa.a.dao;

import java.util.Optional;

import fr.unice.polytech.si5.soa.a.entities.UberooOrder;

/**
 * Class name	IOrderTaker
 * Date			29/09/2018
 * @author		PierreRainero
 *
 */
public interface IOrderTakerDao {
	/**
	 * Add an {@link UberooOrder} into the database
	 * @param orderToAdd order to add
	 * @return the saved order
	 */
	UberooOrder addOrder(UberooOrder orderToAdd);
	
	/**
	 * Update an {@link UberooOrder} into the database
	 * @param orderToUpdate order to update
	 * @return the updated order
	 */
	UberooOrder updateOrder(UberooOrder orderToUpdate);
	
	/**
	 * Search an order in the database using his id
	 * @param orderId id to search
	 * @return the order wrapped in an {@link Optional} if the order exists, Optional.empty() otherwise
	 */
	Optional<UberooOrder> findOrderById(int orderId);
}
