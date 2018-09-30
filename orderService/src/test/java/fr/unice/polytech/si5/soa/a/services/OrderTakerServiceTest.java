package fr.unice.polytech.si5.soa.a.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import static org.mockito.Mockito.*;

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
import fr.unice.polytech.si5.soa.a.entities.Command;
import fr.unice.polytech.si5.soa.a.entities.Meal;

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
	@InjectMocks
    private IOrderTakerService orderService;
	
	private Meal ramen;
	private Command bobCommand;
	
	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		Mockito.reset(orderDaoMock);
		
		ramen = new Meal();
		ramen.setName("Ramen soup");
		
		bobCommand = new Command();
		bobCommand.addMeal(ramen);
	}
	
	@AfterEach
	public void cleanUp() throws Exception {

			bobCommand = null;
			ramen = null;
	}
	
	@Test
	public void addANewCommand() {
		 when(orderDaoMock.addCommand(any(Command.class))).thenReturn(bobCommand);
		 
		 CommandDTO commandDTO = orderService.addCommand(bobCommand.toDTO());
		 
		 assertNotNull(commandDTO);
		 assertEquals(1, commandDTO.getMeals().size());
		 assertEquals(ramen.getName(), commandDTO.getMeals().get(0).getName());
	}
}
