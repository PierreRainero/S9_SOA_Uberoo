package fr.unice.polytech.si5.soa.a.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import fr.unice.polytech.si5.soa.a.communication.CommandDTO;
import fr.unice.polytech.si5.soa.a.configuration.TestConfiguration;
import fr.unice.polytech.si5.soa.a.configuration.WebApplicationConfiguration;
import fr.unice.polytech.si5.soa.a.entities.Command;
import fr.unice.polytech.si5.soa.a.entities.Meal;
import fr.unice.polytech.si5.soa.a.entities.User;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowUserException;
import fr.unice.polytech.si5.soa.a.services.IOrderTakerService;
import fr.unice.polytech.si5.soa.a.util.TestUtil;

/**
 * Class name	OrderTakerControllerTest
 * Date			30/09/2018
 * @author		PierreRainero
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfiguration.class, WebApplicationConfiguration.class})
@WebAppConfiguration
public class OrderTakerControllerTest {
	private final static String BASE_URI = "/orders/";
	private MockMvc mockMvc;
	
	@Qualifier("mock")
	@Autowired
	@Mock
    private IOrderTakerService orderServiceMock;
    
    @Autowired
    @InjectMocks
    private OrderTakerController orderTakerController;
    
	private CommandDTO bobCommand;
    
    @BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		Mockito.reset(orderServiceMock);
		mockMvc = MockMvcBuilders.standaloneSetup(orderTakerController).build();
		
		Meal ramen = new Meal();
		ramen.setName("Ramen soup");
		
		User bob = new User();
		bob.setFirstName("Bob");
		bob.setLastName("Harington");
		
		Command command  = new Command();
		command.addMeal(ramen);
		command.setDeliveryAddress("930 Route des Colles, 06410 Biot");
		command.setTransmitter(bob);
		
		bobCommand = command.toDTO();
	}
    
    @Test
    public void addCommandUsingHTTPPost() throws Exception {
		when(orderServiceMock.addCommand(any(CommandDTO.class))).thenReturn(bobCommand);
		
		mockMvc.perform(post(BASE_URI)
               .contentType(TestUtil.APPLICATION_JSON_UTF8)
               .content(TestUtil.convertObjectToJsonBytes(bobCommand))
        ).andExpect(status().isOk())
         .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8));
        
        ArgumentCaptor<CommandDTO> captor = ArgumentCaptor.forClass(CommandDTO.class);
        verify(orderServiceMock, times(1)).addCommand(captor.capture());
        verifyNoMoreInteractions(orderServiceMock);
        
        CommandDTO commandAdded = captor.getValue();
        assertNotNull(commandAdded);
        assertEquals(1, commandAdded.getMeals().size());
	}
    
    @Test
    public void addCommandWithoutTransmitterUsingHTTPPost() throws Exception {
		when(orderServiceMock.addCommand(any(CommandDTO.class))).thenThrow(UnknowUserException.class);
		
		mockMvc.perform(post(BASE_URI)
               .contentType(TestUtil.APPLICATION_JSON_UTF8)
               .content(TestUtil.convertObjectToJsonBytes(bobCommand))
        ).andExpect(status().isNotFound());
        
        ArgumentCaptor<CommandDTO> captor = ArgumentCaptor.forClass(CommandDTO.class);
        verify(orderServiceMock, times(1)).addCommand(captor.capture());
        verifyNoMoreInteractions(orderServiceMock);
	}
}
