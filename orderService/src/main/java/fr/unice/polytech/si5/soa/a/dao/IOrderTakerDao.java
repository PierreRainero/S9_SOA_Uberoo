package fr.unice.polytech.si5.soa.a.dao;

import fr.unice.polytech.si5.soa.a.entities.Order;

/**
 * Class name	IOrderTaker
 * Date			29/09/2018
 * @author		PierreRainero
 *
 */
public interface IOrderTakerDao {
	/**
	 * Add a {@link Order} into the database
	 * @param orderToAdd command to add
	 * @return the saved command
	 */
	Order addOrder(Order orderToAdd);
}
