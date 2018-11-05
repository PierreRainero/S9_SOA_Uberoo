package fr.unice.polytech.si5.soa.a.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
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

import fr.unice.polytech.si5.soa.a.communication.FeedbackDTO;
import fr.unice.polytech.si5.soa.a.communication.MealDTO;
import fr.unice.polytech.si5.soa.a.communication.bus.MessageProducer;
import fr.unice.polytech.si5.soa.a.communication.bus.messages.Message;
import fr.unice.polytech.si5.soa.a.configuration.TestConfiguration;
import fr.unice.polytech.si5.soa.a.dao.IFeedbackDao;
import fr.unice.polytech.si5.soa.a.dao.IMealDao;
import fr.unice.polytech.si5.soa.a.dao.IRestaurantDao;
import fr.unice.polytech.si5.soa.a.entities.Feedback;
import fr.unice.polytech.si5.soa.a.entities.Ingredient;
import fr.unice.polytech.si5.soa.a.entities.Meal;
import fr.unice.polytech.si5.soa.a.entities.Restaurant;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowRestaurantException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowMealException;
import fr.unice.polytech.si5.soa.a.services.component.MealServiceImpl;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfiguration.class})
public class MealServiceTest {
	@Autowired
	@Qualifier("mock")
	@Mock
	private IMealDao mealDaoMock;

	@Autowired
	@Qualifier("mock")
	@Mock
	private IRestaurantDao restaurantDaoMock;
	
	@Autowired
	@Qualifier("mock")
	@Mock
    private IFeedbackDao feedbackDaoMock;

	@Autowired
	@Mock
	private MessageProducer messageProducerMock;

	@Autowired
	@InjectMocks
	private MealServiceImpl mealService;

	private Restaurant asianRestaurant;
	private Meal ramen;
	private Ingredient pork;
	private Feedback feedback;

	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		Mockito.reset(mealDaoMock);
		Mockito.reset(restaurantDaoMock);
		Mockito.reset(messageProducerMock);
		Mockito.reset(feedbackDaoMock);

		asianRestaurant = new Restaurant();
		asianRestaurant.setName("RizRiz");
		asianRestaurant.setRestaurantAddress("47 avenue des bols");

		ramen = new Meal();
		ramen.setName("Ramen soup");
		ramen.setPrice(10);
		ramen.setRestaurant(asianRestaurant);

		pork = new Ingredient();
		pork.setName("Porc");
		
		feedback = new Feedback();
		feedback.setAuthor("John");
		feedback.setContent("Bof");
		feedback.setMeal(ramen);
	}

	@Test
	public void addMeal() throws Exception {
		when(restaurantDaoMock.findRestaurantById(anyInt())).thenReturn(Optional.of(asianRestaurant));
		when(mealDaoMock.findIngredientByName(anyString())).thenReturn(Optional.of(pork));
		when(mealDaoMock.addMeal(any(Meal.class))).thenReturn(ramen);
		MessageProducer spy = Mockito.spy(messageProducerMock);
		doNothing().when(spy).sendMessage(any(Message.class));

		MealDTO meal = mealService.addMeal(ramen.toDTO(), asianRestaurant.getId());
		assertNotNull(meal);
	}

	@Test
	public void addMealWithNonExistingRestaurant() throws Exception {
		when(restaurantDaoMock.findRestaurantById(anyInt())).thenReturn(Optional.empty());

		assertThrows(UnknowRestaurantException.class, () -> {
			mealService.addMeal(ramen.toDTO(), asianRestaurant.getId());
		});
	}

	@Test
	public void addMealWithNonExistingIngredient() throws Exception {
		when(restaurantDaoMock.findRestaurantById(anyInt())).thenReturn(Optional.of(asianRestaurant));
		when(mealDaoMock.findIngredientByName(anyString())).thenReturn(Optional.empty());
		when(mealDaoMock.addIngredient(any(Ingredient.class))).thenReturn(pork);
		when(mealDaoMock.addMeal(any(Meal.class))).thenReturn(ramen);
		MessageProducer spy = Mockito.spy(messageProducerMock);
		doNothing().when(spy).sendMessage(any(Message.class));

		MealDTO meal = mealService.addMeal(ramen.toDTO(), asianRestaurant.getId());
		assertNotNull(meal);
	}
	
	@Test
	public void findFeedbackForMeal() throws Exception {
		List<Feedback> resultMocked = new ArrayList<>();
		resultMocked.add(feedback);
		when(mealDaoMock.findMealById(anyInt())).thenReturn(Optional.of(ramen));
		when(feedbackDaoMock.findFeedBackByMeal(any(Meal.class))).thenReturn(resultMocked);
		
		List<FeedbackDTO> result = mealService.findFeedbackForMeal(ramen.getId());
		assertNotNull(result);
		assertEquals(1, result.size());
	}
	
	@Test
	public void findFeedBackForNonExistingMeal() {
		when(mealDaoMock.findMealById(anyInt())).thenReturn(Optional.empty());
		
		assertThrows(UnknowMealException.class, () -> {
			mealService.findFeedbackForMeal(ramen.getId());
		});
	}
	
	@Test
	public void addANewFeedback() throws Exception {
		when(mealDaoMock.findMealById(anyInt())).thenReturn(Optional.of(ramen));
		when(feedbackDaoMock.addFeedback(any(Feedback.class))).thenReturn(feedback);
		
		FeedbackDTO result = mealService.addFeedback(feedback.toDTO(), ramen.getId());
		assertNotNull(result);
		assertEquals(feedback.getContent(), result.getContent());
	}
	
	@Test
	public void addFeedbackWithoutAnyId() throws Exception {
		when(restaurantDaoMock.findRestaurant(anyString(), anyString())).thenReturn(Optional.of(asianRestaurant));
		when(mealDaoMock.findMealByNameForRestaurant(anyString(), any(Restaurant.class))).thenReturn(Optional.of(ramen));
		when(feedbackDaoMock.addFeedback(any(Feedback.class))).thenReturn(feedback);
		
		FeedbackDTO result = mealService.addFeedback(feedback.toDTO(), ramen.getName(), asianRestaurant.getName(), asianRestaurant.getRestaurantAddress());
		assertNotNull(result);
		assertEquals(feedback.getContent(), result.getContent());
	}
	
	@Test
	public void addFeedbackWithoutAnyIdWithUnknowMeal() {
		when(restaurantDaoMock.findRestaurant(anyString(), anyString())).thenReturn(Optional.empty());
		when(mealDaoMock.findMealByNameForRestaurant(anyString(), any(Restaurant.class))).thenReturn(Optional.of(ramen));
		when(feedbackDaoMock.addFeedback(any(Feedback.class))).thenReturn(feedback);
		
		assertThrows(UnknowRestaurantException.class, () -> {
			mealService.addFeedback(feedback.toDTO(), ramen.getName(), asianRestaurant.getName(), asianRestaurant.getRestaurantAddress());
		});
	}
	
	@Test
	public void addFeedbackWithoutAnyIdWithUnknowRestaurant() {
		when(restaurantDaoMock.findRestaurant(anyString(), anyString())).thenReturn(Optional.of(asianRestaurant));
		when(mealDaoMock.findMealByNameForRestaurant(anyString(), any(Restaurant.class))).thenReturn(Optional.empty());
		when(feedbackDaoMock.addFeedback(any(Feedback.class))).thenReturn(feedback);
		
		assertThrows(UnknowMealException.class, () -> {
			mealService.addFeedback(feedback.toDTO(), ramen.getName(), asianRestaurant.getName(), asianRestaurant.getRestaurantAddress());
		});
	}
}
