package fr.unice.polytech.si5.soa.a.services.component;

import fr.unice.polytech.si5.soa.a.communication.OrderDTO;
import fr.unice.polytech.si5.soa.a.dao.IOrderTakerDao;
import fr.unice.polytech.si5.soa.a.entities.OrderToPrepare;
import fr.unice.polytech.si5.soa.a.exceptions.InvalidOrderIDException;
import fr.unice.polytech.si5.soa.a.exceptions.NoMealException;
import fr.unice.polytech.si5.soa.a.services.IOrderTakerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Class name	OrderTakerServiceImpl
 *
 * @author PierreRainero
 * @see IOrderTakerService
 * Date			30/09/2018
 **/
@Primary
@Service("OrderTakerService")
public class OrderTakerServiceImpl implements IOrderTakerService {
	@Autowired
	private IOrderTakerDao orderDao;

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public OrderDTO addOrder(OrderDTO orderToAdd) throws NoMealException {
		OrderToPrepare order = new OrderToPrepare(orderToAdd);

		if (order.getMeals().isEmpty()) {
			throw new NoMealException("No meals found in order");
		}

		return orderDao.addOrder(order).toDTO();
	}

	@Override
	public List<OrderDTO> getOrdersToDo() {
		List<OrderToPrepare> ordersToPrepare = orderDao.getOrdersToDo();
		List<OrderDTO> orderDTOS = new ArrayList<>();
		for(OrderToPrepare order: ordersToPrepare ){
			orderDTOS.add(order.toDTO());
		}
		return orderDTOS;
	}

	@Override
	public OrderDTO markOrderAsComplete(int orderID) throws InvalidOrderIDException {
		Optional<OrderToPrepare> order = orderDao.findOrderById(orderID);
		if (order.isPresent()) {
			OrderToPrepare orderToPrepare = order.get();
			orderToPrepare.setDone(true);
			orderDao.updateOrder(orderToPrepare);
			return order.get().toDTO();
		}
		throw new InvalidOrderIDException(orderID + " is not a valid ID");
	}
}