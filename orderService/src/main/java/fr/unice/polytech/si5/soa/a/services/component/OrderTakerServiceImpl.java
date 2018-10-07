package fr.unice.polytech.si5.soa.a.services.component;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import fr.unice.polytech.si5.soa.a.communication.MealDTO;
import fr.unice.polytech.si5.soa.a.communication.Message;
import fr.unice.polytech.si5.soa.a.communication.NewOrder;
import fr.unice.polytech.si5.soa.a.communication.OrderDTO;
import fr.unice.polytech.si5.soa.a.dao.ICatalogDao;
import fr.unice.polytech.si5.soa.a.dao.IOrderTakerDao;
import fr.unice.polytech.si5.soa.a.dao.IUserDao;
import fr.unice.polytech.si5.soa.a.entities.UberooOrder;
import fr.unice.polytech.si5.soa.a.entities.OrderState;
import fr.unice.polytech.si5.soa.a.entities.User;
import fr.unice.polytech.si5.soa.a.entities.Meal;
import fr.unice.polytech.si5.soa.a.exceptions.EmptyDeliveryAddressException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowMealException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowOrderException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowUserException;
import fr.unice.polytech.si5.soa.a.services.IOrderTakerService;

/**
 * Class name	OrderTakerServiceImpl
 * @see 		IOrderTakerService
 * Date			30/09/2018
 * @author		PierreRainero
 **/
@Primary
@Service("OrderTakerService")
public class OrderTakerServiceImpl implements IOrderTakerService {
	@Autowired
	private IOrderTakerDao orderDao;

	@Autowired
	private IUserDao userDao;

	@Autowired
	private ICatalogDao catalogDao;
	
	@Autowired
	private RestTemplate restTemplate;

	@Override
	/**
	 * {@inheritDoc}
	 */
	public OrderDTO addOrder(OrderDTO orderToAdd) throws UnknowUserException, EmptyDeliveryAddressException, UnknowMealException {
		UberooOrder order = new UberooOrder(orderToAdd);

		if(order.getDeliveryAddress()==null || order.getDeliveryAddress().isEmpty()) {
			throw new EmptyDeliveryAddressException("Delivery address cannot be empty");
		}

		Optional<User> userWrapped = userDao.findUserById(orderToAdd.getTransmitter().getId());
		if(!userWrapped.isPresent()) {
			throw new UnknowUserException("Can't find user with id = "+orderToAdd.getTransmitter().getId());
		}
		order.setTransmitter(userWrapped.get());

		findMeals(order, orderToAdd.getMeals());
		order.calculateEta();

		return orderDao.addOrder(order).toDTO();
	}

	private void findMeals(UberooOrder order, List<MealDTO> mealsDTO) throws UnknowMealException{
		for(MealDTO mealDTO : mealsDTO) {
			Optional<Meal> tmp = catalogDao.findMealByName(mealDTO.getName());

			if(!tmp.isPresent()) {
				throw new UnknowMealException("Can't find meal nammed \""+mealDTO.getName()+"\"");
			}

			order.addMeal(tmp.get());
		}
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public OrderDTO updateOrderState(OrderDTO orderToUpdate) throws UnknowOrderException {
		Optional<UberooOrder> orderWrapped = orderDao.findOrderById(orderToUpdate.getId());
		if(!orderWrapped.isPresent()) {
			throw new UnknowOrderException("Can't find order with id = "+orderToUpdate.getId());
		}

		UberooOrder order = orderWrapped.get();
		order.setState(orderToUpdate.getState());

		order = orderDao.updateOrder(order);
		OrderDTO result = order.toDTO();

		if(order.getState().equals(OrderState.VALIDATED)) {
			NewOrder message = new NewOrder(result);
			restTemplate.postForObject("http://messageBus:5001/message", message, Message.class);
		}

		return result;
	}
}