package fr.unice.polytech.si5.soa.a.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

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
import fr.unice.polytech.si5.soa.a.configuration.TestConfiguration;
import fr.unice.polytech.si5.soa.a.dao.IRestaurantDao;
import fr.unice.polytech.si5.soa.a.entities.Restaurant;
import fr.unice.polytech.si5.soa.a.services.component.RestaurantServiceImpl;

/**
 * Class name	RestaurantServiceTest
 * Date			20/10/2018
 * @author 		PierreRainero
 *
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfiguration.class})
public class RestaurantServiceTest {
	@Autowired
	@Qualifier("mock")
	@Mock
	private IRestaurantDao restaurantDaoMock;
	
	@Autowired
	@InjectMocks
	private RestaurantServiceImpl restaurantService;
	
	private Restaurant asianRestaurant;
	
	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		Mockito.reset(restaurantDaoMock);
		
		asianRestaurant = new Restaurant();
		asianRestaurant.setName("Lion d'or");
		asianRestaurant.setRestaurantAddress("22 rue des nems");
	}
	
	@Test
	public void searchRestaurantByName() {
		List<Restaurant> expectedMock = new ArrayList<>();
		expectedMock.add(asianRestaurant);
		when(restaurantDaoMock.findRestaurantByName(anyString())).thenReturn(expectedMock);
		
		List<RestaurantDTO> result = restaurantService.findRestaurantByName(asianRestaurant.getName());
		assertEquals(1, result.size());
	}
}
