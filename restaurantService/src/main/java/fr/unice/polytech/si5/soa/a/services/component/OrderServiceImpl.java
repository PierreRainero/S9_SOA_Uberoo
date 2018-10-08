package fr.unice.polytech.si5.soa.a.services.component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import fr.unice.polytech.si5.soa.a.communication.RestaurantOrderDTO;
import fr.unice.polytech.si5.soa.a.dao.IOrderDao;
import fr.unice.polytech.si5.soa.a.entities.RestaurantOrder;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowOrderException;
import fr.unice.polytech.si5.soa.a.services.IOrderService;

/**
 * Class name	OrderServiceImpl
 * @see			IOrderService
 * Date			08/10/2018
 * @author		PierreRainero
 */
@Primary
@Service("OrderService")
public class OrderServiceImpl implements IOrderService {
	@Autowired
	private IOrderDao orderDao;
	
	@Override
	public RestaurantOrderDTO addOrder(RestaurantOrderDTO orderToAdd) {
		return orderDao.addOrder(new RestaurantOrder(orderToAdd)).toDTO();
	}

	@Override
	public RestaurantOrderDTO updateOrder(RestaurantOrderDTO orderToUpdate) throws UnknowOrderException {
		Optional<RestaurantOrder> deliveryWrapped = orderDao.findOrderById(orderToUpdate.getId());
		if(!deliveryWrapped.isPresent()) {
			throw new UnknowOrderException("Can't find order with id = "+orderToUpdate.getId());
		}
		RestaurantOrder order = deliveryWrapped.get();
		order.setState(orderToUpdate.getState());
		
		return orderDao.updateOrder(order).toDTO();
	}

	@Override
	public List<RestaurantOrderDTO> getOrdersToDo() {
		return orderDao.getOrdersToDo().stream().map(order -> order.toDTO()).collect(Collectors.toList());
	}

}
