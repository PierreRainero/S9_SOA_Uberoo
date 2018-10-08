package fr.unice.polytech.si5.soa.a.services;

import fr.unice.polytech.si5.soa.a.communication.OrderDTO;
import fr.unice.polytech.si5.soa.a.exceptions.InvalidOrderIDException;
import fr.unice.polytech.si5.soa.a.exceptions.NoMealException;

import java.util.List;

/**
 * Class name	IOrderTakerService
 * Date			30/09/2018
 * @author		PierreRainero
 *
 */
public interface IOrderTakerService {
	OrderDTO addOrder(OrderDTO orderToAdd) throws NoMealException;

	List<OrderDTO> getOrdersToDo();

	OrderDTO markOrderAsComplete(int order) throws InvalidOrderIDException;
}
