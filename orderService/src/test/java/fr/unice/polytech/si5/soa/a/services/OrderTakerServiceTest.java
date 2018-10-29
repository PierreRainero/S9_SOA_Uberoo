package fr.unice.polytech.si5.soa.a.services;

import fr.unice.polytech.si5.soa.a.communication.OrderDTO;
import fr.unice.polytech.si5.soa.a.communication.bus.MessageProducer;
import fr.unice.polytech.si5.soa.a.communication.bus.messages.Message;
import fr.unice.polytech.si5.soa.a.configuration.TestConfiguration;
import fr.unice.polytech.si5.soa.a.dao.ICatalogDao;
import fr.unice.polytech.si5.soa.a.dao.IOrderTakerDao;
import fr.unice.polytech.si5.soa.a.dao.IRestaurantDao;
import fr.unice.polytech.si5.soa.a.dao.IUserDao;
import fr.unice.polytech.si5.soa.a.entities.*;
import fr.unice.polytech.si5.soa.a.entities.states.OrderState;
import fr.unice.polytech.si5.soa.a.exceptions.EmptyDeliveryAddressException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowMealException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowOrderException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowUserException;
import fr.unice.polytech.si5.soa.a.services.component.OrderTakerServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Class name	OrderTakerServiceTest
 * Date			30/09/2018
 *
 * @author 		PierreRainero
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfiguration.class})
public class OrderTakerServiceTest {
	@Autowired
	@Qualifier("mock")
	@Mock
	private IOrderTakerDao orderDaoMock;

	@Autowired
	@Qualifier("mock")
	@Mock
	private IUserDao userDaoMock;

	@Autowired
	@Qualifier("mock")
	@Mock
	private ICatalogDao catalogDaoMock;

	@Autowired
	@Qualifier("mock")
	@Mock
	private IRestaurantDao restaurantDaoMock;

	@Autowired
	@Mock
	private MessageProducer messageProducerMock;

	@Autowired
	@InjectMocks
	private OrderTakerServiceImpl orderService;

	private Restaurant asianRestaurant;
	private Meal ramen;
	private UberooOrder bobOrder;
	private User bob;

	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		Mockito.reset(orderDaoMock);
		Mockito.reset(userDaoMock);
		Mockito.reset(catalogDaoMock);
		Mockito.reset(messageProducerMock);
		
		asianRestaurant = new Restaurant();
		asianRestaurant.setName("Lion d'or");
		asianRestaurant.setRestaurantAddress("22 rue des nems");

		ramen = new Meal();
		ramen.setName("Ramen soup");
		ramen.setRestaurant(asianRestaurant);

		bob = new User();
		bob.setFirstName("Bob");
		bob.setLastName("Harington");

		bobOrder = new UberooOrder();
		bobOrder.addMeal(ramen);
		bobOrder.setRestaurant(asianRestaurant);
		bobOrder.setDeliveryAddress("930 Route des Colles, 06410 Biot");
		bobOrder.setTransmitter(bob);
	}

	@AfterEach
	public void cleanUp() throws Exception {
		bobOrder = null;
		ramen = null;
	}

	@Test
	public void addANewOrder() throws Exception {
		when(orderDaoMock.addOrder(any(UberooOrder.class))).thenReturn(bobOrder);
		when(userDaoMock.findUserById(anyInt())).thenReturn(Optional.of(bob));
		when(catalogDaoMock.findMealByName(anyString())).thenReturn(Optional.of(ramen));
		when(restaurantDaoMock.findRestaurantById(anyInt())).thenReturn(Optional.of(asianRestaurant));

		OrderDTO orderDTO = orderService.addOrder(bobOrder.toDTO());

		assertNotNull(orderDTO);
		assertEquals(1, orderDTO.getMeals().size());
		assertEquals(ramen.getName(), orderDTO.getMeals().get(0).getName());
	}

	@Test
	public void addANewOrderWithoutTransmitter() throws Exception {
		when(orderDaoMock.addOrder(any(UberooOrder.class))).thenReturn(bobOrder);
		when(userDaoMock.findUserById(anyInt())).thenReturn(Optional.empty());
		when(catalogDaoMock.findMealByName(anyString())).thenReturn(Optional.of(ramen));

		assertThrows(UnknowUserException.class, () -> {
			orderService.addOrder(bobOrder.toDTO());
		});
	}

	@Test
	public void addANewOrderWithoutDeliveryAddress() throws Exception {
		bobOrder.setDeliveryAddress("");
		when(orderDaoMock.addOrder(any(UberooOrder.class))).thenReturn(bobOrder);
		when(userDaoMock.findUserById(anyInt())).thenReturn(Optional.of(bob));
		when(catalogDaoMock.findMealByName(anyString())).thenReturn(Optional.of(ramen));

		assertThrows(EmptyDeliveryAddressException.class, () -> {
			orderService.addOrder(bobOrder.toDTO());
		});
	}

	@Test
	public void addANewOrderWithNonExistingMeal() throws Exception {
		when(orderDaoMock.addOrder(any(UberooOrder.class))).thenReturn(bobOrder);
		when(userDaoMock.findUserById(anyInt())).thenReturn(Optional.of(bob));
		when(catalogDaoMock.findMealByName(anyString())).thenReturn(Optional.empty());
		when(restaurantDaoMock.findRestaurantById(anyInt())).thenReturn(Optional.of(asianRestaurant));

		assertThrows(UnknowMealException.class, () -> {
			orderService.addOrder(bobOrder.toDTO());
		});
	}

	@Test
	public void validateAnOrder() throws Exception {
		when(orderDaoMock.findOrderById(anyInt())).thenReturn(Optional.of(bobOrder));
		when(orderDaoMock.updateOrder(any(UberooOrder.class))).thenReturn(bobOrder);
		MessageProducer spy = Mockito.spy(messageProducerMock);
		doNothing().when(spy).sendMessage(any(Message.class));

		bobOrder.setState(OrderState.VALIDATED);
		OrderDTO order = orderService.updateOrderState(bobOrder.toDTO());

		assertEquals(bobOrder.getDeliveryAddress(), order.getDeliveryAddress());
		assertEquals(OrderState.VALIDATED, order.getState());
	}

	@Test
	public void refuseAnOrder() throws Exception {
		when(orderDaoMock.findOrderById(anyInt())).thenReturn(Optional.of(bobOrder));
		when(orderDaoMock.updateOrder(any(UberooOrder.class))).thenReturn(bobOrder);
		assertEquals(OrderState.WAITING, bobOrder.getState());

		bobOrder.setState(OrderState.REFUSED);
		OrderDTO order = orderService.updateOrderState(bobOrder.toDTO());

		assertEquals(bobOrder.getDeliveryAddress(), order.getDeliveryAddress());
		assertEquals(OrderState.REFUSED, order.getState());
	}

	@Test
	public void updateNonExistingOrder() throws Exception {
		when(orderDaoMock.findOrderById(anyInt())).thenReturn(Optional.empty());
		when(orderDaoMock.updateOrder(any(UberooOrder.class))).thenReturn(bobOrder);

		assertThrows(UnknowOrderException.class, () -> {
			orderService.updateOrderState(bobOrder.toDTO());
		});
	}
	
	@Test
	public void findExistingOrderById() throws Exception {
		when(orderDaoMock.findOrderById(anyInt())).thenReturn(Optional.of(bobOrder));
		
		OrderDTO orderDTO = orderService.findOrderById(-1);
		
		assertNotNull(orderDTO);
		assertEquals(1, orderDTO.getMeals().size());
		assertEquals(ramen.getName(), orderDTO.getMeals().get(0).getName());
	}
	
	@Test
	public void findNonExistingOrderById() {
		when(orderDaoMock.findOrderById(anyInt())).thenReturn(Optional.empty());
		
		assertThrows(UnknowOrderException.class, () -> {
			orderService.findOrderById(-1);
		});
	}
}
