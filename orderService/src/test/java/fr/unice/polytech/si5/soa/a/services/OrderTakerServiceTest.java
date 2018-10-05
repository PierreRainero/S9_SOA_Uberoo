package fr.unice.polytech.si5.soa.a.services;

import fr.unice.polytech.si5.soa.a.dto.OrderDTO;
import fr.unice.polytech.si5.soa.a.configuration.TestConfiguration;
import fr.unice.polytech.si5.soa.a.dao.ICatalogDao;
import fr.unice.polytech.si5.soa.a.dao.IOrderTakerDao;
import fr.unice.polytech.si5.soa.a.dao.IUserDao;
import fr.unice.polytech.si5.soa.a.entities.Meal;
import fr.unice.polytech.si5.soa.a.entities.Order;
import fr.unice.polytech.si5.soa.a.entities.User;
import fr.unice.polytech.si5.soa.a.exceptions.EmptyDeliveryAddressException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowMealException;
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
 * @author		PierreRainero
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { TestConfiguration.class })
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
	@InjectMocks
	private OrderTakerServiceImpl orderService;

	private Meal ramen;
	private Order bobCommand;
	private User bob;

	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		Mockito.reset(orderDaoMock);
		Mockito.reset(userDaoMock);
		Mockito.reset(catalogDaoMock);

		ramen = new Meal();
		ramen.setName("Ramen soup");

		bob = new User();
		bob.setFirstName("Bob");
		bob.setLastName("Harington");

		bobCommand = new Order();
		bobCommand.addMeal(ramen);
		bobCommand.setDeliveryAddress("930 Route des Colles, 06410 Biot");
		bobCommand.setTransmitter(bob);
	}

	@AfterEach
	public void cleanUp() throws Exception {
		bobCommand = null;
		ramen = null;
	}

	@Test
	public void addANewOrder() throws Exception {
		when(orderDaoMock.addOrder(any(Order.class))).thenReturn(bobCommand);
		when(userDaoMock.findUserById(anyInt())).thenReturn(Optional.of(bob));
		when(catalogDaoMock.findMealByName(anyString())).thenReturn(Optional.of(ramen));

		OrderDTO orderDTO = orderService.addOrder(bobCommand.toDTO());

		assertNotNull(orderDTO);
		assertEquals(1, orderDTO.getMeals().size());
		assertEquals(ramen.getName(), orderDTO.getMeals().get(0).getName());
	}

	@Test
	public void addANewOrderWithoutTransmitter() throws Exception {
		when(orderDaoMock.addOrder(any(Order.class))).thenReturn(bobCommand);
		when(userDaoMock.findUserById(anyInt())).thenReturn(Optional.empty());
		when(catalogDaoMock.findMealByName(anyString())).thenReturn(Optional.of(ramen));

		assertThrows(UnknowUserException.class,()->{
			orderService.addOrder(bobCommand.toDTO());
		});
	}

	@Test
	public void addANewOrderWithoutDeliveryAddress() throws Exception {
		bobCommand.setDeliveryAddress("");
		when(orderDaoMock.addOrder(any(Order.class))).thenReturn(bobCommand);
		when(userDaoMock.findUserById(anyInt())).thenReturn(Optional.of(bob));
		when(catalogDaoMock.findMealByName(anyString())).thenReturn(Optional.of(ramen));

		assertThrows(EmptyDeliveryAddressException.class,()->{
			orderService.addOrder(bobCommand.toDTO());
		});
	}
	
	@Test
	public void addANewOrderWithNonExistingMeal() throws Exception {
		when(orderDaoMock.addOrder(any(Order.class))).thenReturn(bobCommand);
		when(userDaoMock.findUserById(anyInt())).thenReturn(Optional.of(bob));
		when(catalogDaoMock.findMealByName(anyString())).thenReturn(Optional.empty());

		assertThrows(UnknowMealException.class,()->{
			orderService.addOrder(bobCommand.toDTO());
		});
	}
}
