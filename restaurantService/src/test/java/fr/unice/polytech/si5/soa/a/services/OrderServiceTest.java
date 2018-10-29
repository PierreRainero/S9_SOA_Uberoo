package fr.unice.polytech.si5.soa.a.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
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

import fr.unice.polytech.si5.soa.a.communication.RestaurantOrderDTO;
import fr.unice.polytech.si5.soa.a.configuration.TestConfiguration;
import fr.unice.polytech.si5.soa.a.dao.IMealDao;
import fr.unice.polytech.si5.soa.a.dao.IOrderDao;
import fr.unice.polytech.si5.soa.a.dao.IRestaurantDao;
import fr.unice.polytech.si5.soa.a.entities.Meal;
import fr.unice.polytech.si5.soa.a.entities.Restaurant;
import fr.unice.polytech.si5.soa.a.entities.RestaurantOrder;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowMealException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowOrderException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowRestaurantException;
import fr.unice.polytech.si5.soa.a.services.component.OrderServiceImpl;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfiguration.class})
public class OrderServiceTest {
	@Autowired
	@Qualifier("mock")
	@Mock
	private IOrderDao orderDaoMock;
	
	@Autowired
	@Qualifier("mock")
	@Mock
	private IRestaurantDao restaurantDaoMock;
	
	@Autowired
	@Qualifier("mock")
	@Mock
	private IMealDao mealDaoMock;
	
	@Autowired
	@InjectMocks
	private OrderServiceImpl orderService;
	
    private Restaurant asianRestaurant;
    private RestaurantOrder ramenOrder;
    private Meal ramen;
	
	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		Mockito.reset(orderDaoMock);
		Mockito.reset(restaurantDaoMock);
		Mockito.reset(mealDaoMock);
		
		asianRestaurant = new Restaurant();
    	asianRestaurant.setName("RizRiz");
    	asianRestaurant.setRestaurantAddress("47 avenue des bols");
    	
    	ramen = new Meal();
        ramen.setName("Ramen soup");
        ramen.setPrice(10);
        ramen.setRestaurant(asianRestaurant);
        
        ramenOrder = new RestaurantOrder();
        ramenOrder.addMeal(ramen);
        ramenOrder.setRestaurant(asianRestaurant);
	}
	
	@Test
	public void addOrder() throws Exception {
		when(restaurantDaoMock.findRestaurantById(anyInt())).thenReturn(Optional.of(asianRestaurant));
		when(mealDaoMock.findMealById(anyInt())).thenReturn(Optional.of(ramen));
		when(orderDaoMock.addOrder(any(RestaurantOrder.class))).thenReturn(ramenOrder);
		
		RestaurantOrderDTO restautantOrder = orderService.addOrder(ramenOrder.toDTO());
		assertNotNull(restautantOrder);
	}
	
	@Test
	public void addOrderWithNonExistingRestaurant() {
		when(restaurantDaoMock.findRestaurantById(anyInt())).thenReturn(Optional.empty());
		
		assertThrows(UnknowRestaurantException.class, () -> {
			orderService.addOrder(ramenOrder.toDTO());
		});
	}
	
	@Test
	public void addOrderWithNonExistingMeal() {
		when(restaurantDaoMock.findRestaurantById(anyInt())).thenReturn(Optional.of(asianRestaurant));
		when(mealDaoMock.findMealById(anyInt())).thenReturn(Optional.empty());
		
		assertThrows(UnknowMealException.class, () -> {
			orderService.addOrder(ramenOrder.toDTO());
		});
	}
	
	@Test
	public void updateOrder() throws Exception {
		when(orderDaoMock.findOrderById(anyInt())).thenReturn(Optional.of(ramenOrder));
		when(orderDaoMock.updateOrder(any(RestaurantOrder.class))).thenReturn(ramenOrder);
		
		RestaurantOrderDTO restautantOrder = orderService.updateOrder(ramenOrder.toDTO());
		assertNotNull(restautantOrder);
	}
	
	@Test
	public void updateNonExistingOrder() {
		when(orderDaoMock.findOrderById(anyInt())).thenReturn(Optional.empty());
		
		assertThrows(UnknowOrderException.class, () -> {
			orderService.updateOrder(ramenOrder.toDTO());
		});
	}
	
	@Test
	public void getOrdersToDo() throws Exception {
		List<RestaurantOrder> resultMocked = new ArrayList<>();
		resultMocked.add(ramenOrder);
		when(restaurantDaoMock.findRestaurantById(anyInt())).thenReturn(Optional.of(asianRestaurant));
		when(orderDaoMock.getOrdersToDo(any(Restaurant.class))).thenReturn(resultMocked);
		
		List<RestaurantOrderDTO> result = orderService.getOrdersToDo(asianRestaurant.getId());
		assertEquals(resultMocked.size(), result.size());
	}
	
	@Test
	public void getOrdersToDoWithNonExistingRestaurant() {
		when(restaurantDaoMock.findRestaurantById(anyInt())).thenReturn(Optional.empty());
		
		assertThrows(UnknowRestaurantException.class, () -> {
			orderService.getOrdersToDo(asianRestaurant.getId());
		});
	}
	
	@Test
	public void addOrderWithoutDTOObject() throws Exception {
		when(restaurantDaoMock.findRestaurant(anyString(), anyString())).thenReturn(Optional.of(asianRestaurant));
		when(mealDaoMock.findMealByNameForRestaurant(anyString(), any(Restaurant.class))).thenReturn(Optional.of(ramen));
		when(orderDaoMock.addOrder(any(RestaurantOrder.class))).thenReturn(ramenOrder);
		
		List<String> meals = new ArrayList<>();
		meals.add(ramen.getName());
		RestaurantOrderDTO restautantOrder = orderService.addOrder(meals, asianRestaurant.getName(), asianRestaurant.getRestaurantAddress());
		assertNotNull(restautantOrder);
		assertEquals(1, restautantOrder.getMeals().size());
	}
	
	@Test
	public void addOrderWithoutDTOObjectWithUnknowRestaurant() throws Exception {
		when(restaurantDaoMock.findRestaurant(anyString(), anyString())).thenReturn(Optional.empty());
		when(mealDaoMock.findMealByNameForRestaurant(anyString(), any(Restaurant.class))).thenReturn(Optional.of(ramen));
		when(orderDaoMock.addOrder(any(RestaurantOrder.class))).thenReturn(ramenOrder);
		
		List<String> meals = new ArrayList<>();
		meals.add(ramen.getName());
		assertThrows(UnknowRestaurantException.class, () -> {
			orderService.addOrder(meals, asianRestaurant.getName(), asianRestaurant.getRestaurantAddress());
		});
	}
	
	@Test
	public void addOrderWithoutDTOObjectWithUnknowMeal() throws Exception {
		when(restaurantDaoMock.findRestaurant(anyString(), anyString())).thenReturn(Optional.of(asianRestaurant));
		when(mealDaoMock.findMealByNameForRestaurant(anyString(), any(Restaurant.class))).thenReturn(Optional.empty());
		when(orderDaoMock.addOrder(any(RestaurantOrder.class))).thenReturn(ramenOrder);
		
		List<String> meals = new ArrayList<>();
		meals.add(ramen.getName());
		assertThrows(UnknowMealException.class, () -> {
			orderService.addOrder(meals, asianRestaurant.getName(), asianRestaurant.getRestaurantAddress());
		});
	}
}
