package fr.unice.polytech.si5.soa.a.services.component;

import fr.unice.polytech.si5.soa.a.communication.MealDTO;
import fr.unice.polytech.si5.soa.a.communication.OrderDTO;
import fr.unice.polytech.si5.soa.a.communication.bus.MessageProducer;
import fr.unice.polytech.si5.soa.a.communication.bus.messages.NewOrder;
import fr.unice.polytech.si5.soa.a.dao.ICatalogDao;
import fr.unice.polytech.si5.soa.a.dao.IOrderTakerDao;
import fr.unice.polytech.si5.soa.a.dao.IRestaurantDao;
import fr.unice.polytech.si5.soa.a.dao.IUserDao;
import fr.unice.polytech.si5.soa.a.entities.*;
import fr.unice.polytech.si5.soa.a.entities.states.OrderState;
import fr.unice.polytech.si5.soa.a.exceptions.*;
import fr.unice.polytech.si5.soa.a.services.IOrderTakerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Class name	OrderTakerServiceImpl
 *
 * @author 		PierreRainero
 * @see 		IOrderTakerService
 * Date			30/09/2018
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
	private IRestaurantDao restaurantDao;

	@Autowired
	private MessageProducer producer;

	@Override
	/**
	 * {@inheritDoc}
	 */
	public OrderDTO addOrder(OrderDTO orderToAdd) throws UnknowUserException, EmptyDeliveryAddressException, UnknowMealException, UnknowRestaurantException {
		UberooOrder order = new UberooOrder(orderToAdd);

		if (order.getDeliveryAddress() == null || order.getDeliveryAddress().isEmpty()) {
			throw new EmptyDeliveryAddressException("Delivery address cannot be empty");
		}

		Optional<User> userWrapped = userDao.findUserById(orderToAdd.getTransmitter().getId());
		if (!userWrapped.isPresent()) {
			throw new UnknowUserException("Can't find user with id = " + orderToAdd.getTransmitter().getId());
		}
		order.setTransmitter(userWrapped.get());

		Optional<Restaurant> restaurantWrapped = restaurantDao.findRestaurantById(orderToAdd.getRestaurant().getId());
		if(!restaurantWrapped.isPresent()){
			throw new UnknowRestaurantException("Can't find restaurant with id = "+orderToAdd.getRestaurant().getId());
		}
		order.setRestaurant(restaurantWrapped.get());
		findMeals(order, orderToAdd.getMeals());
		order.calculateEta();
		return orderDao.addOrder(order).toDTO();
	}

	private void findMeals(UberooOrder order, List<MealDTO> mealsDTO) throws UnknowMealException {
		for (MealDTO mealDTO : mealsDTO) {
			Optional<Meal> tmp = catalogDao.findMealByName(mealDTO.getName());

			if (!tmp.isPresent()) {
				throw new UnknowMealException("Can't find meal nammed \"" + mealDTO.getName() + "\"");
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
		if (!orderWrapped.isPresent()) {
			throw new UnknowOrderException("Can't find order with id = " + orderToUpdate.getId());
		}

		UberooOrder order = orderWrapped.get();
		
		if(orderToUpdate.getState().equals(OrderState.VALIDATED) && order.getState().equals(OrderState.WAITING)) {
			order.setValidationDate(new Date());
		}
		order.setState(orderToUpdate.getState());

		order = orderDao.updateOrder(order);
		OrderDTO result = order.toDTO();

		if (order.getState().equals(OrderState.VALIDATED)) {
			 NewOrder message = new NewOrder(result);
			 producer.sendMessage(message);
		}

		return result;
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public OrderDTO findOrderById(int id) throws UnknowOrderException {
		Optional<UberooOrder> orderWrapped = orderDao.findOrderById(id);
		
		if(!orderWrapped.isPresent()) {
			throw new UnknowOrderException("Can't find order with id = " + id);
		}
		
		return orderWrapped.get().toDTO();
	}
}