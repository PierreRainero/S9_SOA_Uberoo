package fr.unice.polytech.si5.soa.a.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

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

import fr.unice.polytech.si5.soa.a.communication.RestaurantDTO;
import fr.unice.polytech.si5.soa.a.configuration.TestConfiguration;
import fr.unice.polytech.si5.soa.a.configuration.WebApplicationConfiguration;
import fr.unice.polytech.si5.soa.a.entities.Restaurant;
import fr.unice.polytech.si5.soa.a.services.IRestaurantService;
import fr.unice.polytech.si5.soa.a.util.TestUtil;

/**
 * Class name	RestaurantControllerTest
 * Date			20/10/2018
 * @author		PierreRainero
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfiguration.class, WebApplicationConfiguration.class})
@WebAppConfiguration
public class RestaurantControllerTest {
	private final static String BASE_URI = "/restaurants/";
	private MockMvc mockMvc;
	
	private Restaurant asianRestaurant;
	
	@Qualifier("mock")
	@Autowired
	@Mock
    private IRestaurantService restaurantServiceMock;
	
	@Autowired
    @InjectMocks
    private RestaurantController restaurantController;
	
	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		Mockito.reset(restaurantServiceMock);
		
		mockMvc = MockMvcBuilders.standaloneSetup(restaurantController).build();
		
		asianRestaurant = new Restaurant();
		asianRestaurant.setName("Lion d'or");
		asianRestaurant.setRestaurantAddress("22 rue des nems");
	}
	
	@Test
    public void searchLionRestaurantUsingHTTPGet() throws Exception {
		List<RestaurantDTO> expectedMock = new ArrayList<>();
		expectedMock.add(asianRestaurant.toDTO());
		when(restaurantServiceMock.findRestaurantByName(anyString())).thenReturn(expectedMock);
		
		mockMvc.perform(get(BASE_URI)
               .contentType(TestUtil.APPLICATION_JSON_UTF8)
               .param("name", asianRestaurant.getName())
        ).andExpect(status().isOk())
         .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8));
		
		ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(restaurantServiceMock, times(1)).findRestaurantByName(captor.capture());
        verifyNoMoreInteractions(restaurantServiceMock);
        
        String restaurantName = captor.getValue();
        assertEquals(asianRestaurant.getName(), restaurantName);
	}
	
	@Test
    public void searchRestaurantWitoutNameUsingHTTPGet() throws Exception {
		List<RestaurantDTO> expectedMock = new ArrayList<>();
		expectedMock.add(asianRestaurant.toDTO());
		when(restaurantServiceMock.findRestaurantByName(anyString())).thenReturn(expectedMock);
		
		mockMvc.perform(get(BASE_URI)
               .contentType(TestUtil.APPLICATION_JSON_UTF8)
        ).andExpect(status().isOk())
         .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8));
		
		ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(restaurantServiceMock, times(1)).findRestaurantByName(captor.capture());
        verifyNoMoreInteractions(restaurantServiceMock);
        
        String restaurantName = captor.getValue();
        assertTrue(restaurantName.isEmpty());
	}
}
