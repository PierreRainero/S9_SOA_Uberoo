package fr.unice.polytech.si5.soa.a.dao;

import fr.unice.polytech.si5.soa.a.entities.OrderToPrepare;

import java.util.List;
import java.util.Optional;

/**
 * Class name	IOrderTaker
 * Date			29/09/2018
 * @author		PierreRainero
 *
 */
public interface IOrderTakerDao {

	OrderToPrepare addOrder(OrderToPrepare orderToAdd);

	Optional<OrderToPrepare> findOrderById(int orderId);

	List<OrderToPrepare> getOrdersToDo();

	OrderToPrepare updateOrder(OrderToPrepare orderToUpdate);
}
