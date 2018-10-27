package fr.unice.polytech.si5.soa.a.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Optional;

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

import fr.unice.polytech.si5.soa.a.communication.RestaurantDTO;
import fr.unice.polytech.si5.soa.a.communication.bus.Message;
import fr.unice.polytech.si5.soa.a.communication.bus.MessageProducer;
import fr.unice.polytech.si5.soa.a.configuration.TestConfiguration;
import fr.unice.polytech.si5.soa.a.dao.IRestaurantDao;
import fr.unice.polytech.si5.soa.a.entities.Restaurant;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowRestaurantException;
import fr.unice.polytech.si5.soa.a.services.component.RestaurantServiceImpl;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfiguration.class})
public class RestaurantServiceTest {
	@Autowired
	@Qualifier("mock")
	@Mock
	private IRestaurantDao restaurantDaoMock;
	
	@Autowired
	@Mock
	private MessageProducer messageProducerMock;
	
	@Autowired
	@InjectMocks
	private RestaurantServiceImpl restaurantService;
	
	private Restaurant asianRestaurant;
	
	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		Mockito.reset(restaurantDaoMock);
		Mockito.reset(messageProducerMock);
		
		asianRestaurant = new Restaurant();
    	asianRestaurant.setName("RizRiz");
    	asianRestaurant.setRestaurantAddress("47 avenue des bols");
	}
	
	@Test
	public void addRestaurant() {
		when(restaurantDaoMock.addRestaurant(any(Restaurant.class))).thenReturn(asianRestaurant);
		MessageProducer spy = Mockito.spy(messageProducerMock);
		doNothing().when(spy).sendMessage(any(Message.class));
		
		RestaurantDTO restaurant = restaurantService.addRestaurant(asianRestaurant.toDTO());
		assertNotNull(restaurant);
		assertEquals(asianRestaurant.getName(), restaurant.getName());
	}
	
	@Test
	public void findRestaurantUsingNameAndAddress() throws Exception {
		when(restaurantDaoMock.findRestaurant(anyString(), anyString())).thenReturn(Optional.of(asianRestaurant));
		
		RestaurantDTO restaurant = restaurantService.findRestaurant(asianRestaurant.getName(), asianRestaurant.getRestaurantAddress());
		assertNotNull(restaurant);
		assertEquals(asianRestaurant.getName(), restaurant.getName());
	}
	
	@Test
	public void dontFindRestaurantUsingNameAndAddress() {
		when(restaurantDaoMock.findRestaurant(anyString(), anyString())).thenReturn(Optional.empty());
		
		assertThrows(UnknowRestaurantException.class, () -> {
			restaurantService.findRestaurant(asianRestaurant.getName(), asianRestaurant.getRestaurantAddress());
		});
	}
	
	@Test
	public void findRestaurantById() throws Exception  {
		when(restaurantDaoMock.findRestaurantById(anyInt())).thenReturn(Optional.of(asianRestaurant));
		
		RestaurantDTO restaurant = restaurantService.findRestaurantById(asianRestaurant.getId());
		assertNotNull(restaurant);
		assertEquals(asianRestaurant.getName(), restaurant.getName());
	}
	
	@Test
	public void dontFindRestaurantById() {
		when(restaurantDaoMock.findRestaurantById(anyInt())).thenReturn(Optional.empty());
		
		assertThrows(UnknowRestaurantException.class, () -> {
			restaurantService.findRestaurantById(asianRestaurant.getId());
		});
	}
}
