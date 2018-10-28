package fr.unice.polytech.si5.soa.a.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
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

import fr.unice.polytech.si5.soa.a.communication.MealDTO;
import fr.unice.polytech.si5.soa.a.communication.RestaurantDTO;
import fr.unice.polytech.si5.soa.a.configuration.TestConfiguration;
import fr.unice.polytech.si5.soa.a.dao.IRestaurantDao;
import fr.unice.polytech.si5.soa.a.entities.Meal;
import fr.unice.polytech.si5.soa.a.entities.Restaurant;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowRestaurantException;
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
	private Meal ramen;
	
	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		Mockito.reset(restaurantDaoMock);
		
		asianRestaurant = new Restaurant();
		asianRestaurant.setName("Lion d'or");
		asianRestaurant.setRestaurantAddress("22 rue des nems");
		
		ramen = new Meal();
		ramen.setName("Ramen soup");
		ramen.setRestaurant(asianRestaurant);
		ramen.setPrice(11.5);
	}
	
	@Test
	public void searchRestaurantByName() {
		List<Restaurant> expectedMock = new ArrayList<>();
		expectedMock.add(asianRestaurant);
		when(restaurantDaoMock.findRestaurantByName(anyString())).thenReturn(expectedMock);
		
		List<RestaurantDTO> result = restaurantService.findRestaurantByName(asianRestaurant.getName());
		assertEquals(1, result.size());
	}
	
	@Test
	public void searchRestaurantWithoutName() {
		List<Restaurant> expectedMock = new ArrayList<>();
		expectedMock.add(asianRestaurant);
		when(restaurantDaoMock.listRestaurants()).thenReturn(expectedMock);
		
		List<RestaurantDTO> result = restaurantService.findRestaurantByName("");
		assertEquals(1, result.size());
	}
	
	@Test
	public void addARestaurant() {
		when(restaurantDaoMock.addRestaurant(any(Restaurant.class))).thenReturn(asianRestaurant);
		
		RestaurantDTO restaurant = restaurantService.addRestaurant(asianRestaurant.toDTO());
		assertNotNull(restaurant);
		assertEquals(asianRestaurant.getName(), restaurant.getName());
	}
	
	@Test
	public void addAMeal() throws Exception {
		when(restaurantDaoMock.findRestaurantByNameAndAddress(anyString(), anyString())).thenReturn(Optional.of(asianRestaurant));
		when(restaurantDaoMock.addMeal(any(Meal.class))).thenReturn(ramen);
		
		MealDTO meal = restaurantService.addMeal(ramen.toDTO(), asianRestaurant.getName(), asianRestaurant.getRestaurantAddress());
		assertNotNull(meal);
		assertEquals(ramen.getName(), meal.getName());
	}
	
	@Test
	public void addAMealOnANonExistingRestaurant() {
		when(restaurantDaoMock.findRestaurantByNameAndAddress(anyString(), anyString())).thenReturn(Optional.empty());
		
		assertThrows(UnknowRestaurantException.class, () -> {
			restaurantService.addMeal(ramen.toDTO(), asianRestaurant.getName(), asianRestaurant.getRestaurantAddress());
		});
	}
}
