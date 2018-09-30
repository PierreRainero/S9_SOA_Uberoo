package fr.unice.polytech.si5.soa.a.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;

import fr.unice.polytech.si5.soa.a.communication.CommandDTO;
import fr.unice.polytech.si5.soa.a.configuration.TestConfiguration;
import fr.unice.polytech.si5.soa.a.dao.IOrderTakerDao;
import fr.unice.polytech.si5.soa.a.dao.IUserDao;
import fr.unice.polytech.si5.soa.a.entities.Command;
import fr.unice.polytech.si5.soa.a.entities.Meal;
import fr.unice.polytech.si5.soa.a.entities.User;
import fr.unice.polytech.si5.soa.a.exceptions.EmptyDeliveryAddressException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowUserException;

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
	@InjectMocks
    private IOrderTakerService orderService;
	
	private Meal ramen;
	private Command bobCommand;
	private User bob;
	
	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		Mockito.reset(orderDaoMock);
		Mockito.reset(userDaoMock);
		
		ramen = new Meal();
		ramen.setName("Ramen soup");
		
		bob = new User();
		bob.setFirstName("Bob");
		bob.setLastName("Harington");
		
		bobCommand = new Command();
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
	public void addANewCommand() throws Exception {
		 when(orderDaoMock.addCommand(any(Command.class))).thenReturn(bobCommand);
		 when(userDaoMock.findUserById(anyInt())).thenReturn(Optional.of(bob));
		 
		 CommandDTO commandDTO = orderService.addCommand(bobCommand.toDTO());
		 
		 assertNotNull(commandDTO);
		 assertEquals(1, commandDTO.getMeals().size());
		 assertEquals(ramen.getName(), commandDTO.getMeals().get(0).getName());
	}
	
	@Test
	public void addANewCommandWithoutTransmitter() throws Exception {
		 when(orderDaoMock.addCommand(any(Command.class))).thenReturn(bobCommand);
		 when(userDaoMock.findUserById(anyInt())).thenReturn(Optional.empty());
		 
		 assertThrows(UnknowUserException.class,()->{
			 orderService.addCommand(bobCommand.toDTO());
		});
	}
	
	@Test
	public void addANewCommandWithoutDeliveryAddress() throws Exception {
		bobCommand.setDeliveryAddress("");
		 when(orderDaoMock.addCommand(any(Command.class))).thenReturn(bobCommand);
		 when(userDaoMock.findUserById(anyInt())).thenReturn(Optional.of(bob));
		 
		 assertThrows(EmptyDeliveryAddressException.class,()->{
			 orderService.addCommand(bobCommand.toDTO());
		});
	}
}
