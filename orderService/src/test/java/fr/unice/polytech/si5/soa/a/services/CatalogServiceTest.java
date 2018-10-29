package fr.unice.polytech.si5.soa.a.services;

import fr.unice.polytech.si5.soa.a.communication.FeedbackDTO;
import fr.unice.polytech.si5.soa.a.communication.MealDTO;
import fr.unice.polytech.si5.soa.a.communication.bus.MessageProducer;
import fr.unice.polytech.si5.soa.a.communication.bus.messages.Message;
import fr.unice.polytech.si5.soa.a.configuration.TestConfiguration;
import fr.unice.polytech.si5.soa.a.dao.ICatalogDao;
import fr.unice.polytech.si5.soa.a.dao.IRestaurantDao;
import fr.unice.polytech.si5.soa.a.dao.IUserDao;
import fr.unice.polytech.si5.soa.a.entities.Feedback;
import fr.unice.polytech.si5.soa.a.entities.Meal;
import fr.unice.polytech.si5.soa.a.entities.Restaurant;
import fr.unice.polytech.si5.soa.a.entities.User;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowMealException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowRestaurantException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowUserException;
import fr.unice.polytech.si5.soa.a.services.component.CatalogServiceImpl;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

/**
 * Class name	CatalogServiceTest
 * Date			01/10/2018
 * @author		PierreRainero
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { TestConfiguration.class })
public class CatalogServiceTest {
	private static final String ASIAN_CATEGORY = "Asian";
	
	@Autowired
	@Qualifier("mock")
	@Mock
	private ICatalogDao catalogDaoMock;
	
	@Autowired
	@Qualifier("mock")
	@Mock
	private IUserDao userDaoMock;
	
	@Autowired
	@Qualifier("mock")
	@Mock
	private IRestaurantDao restaurantDaoMock;
	
	@Autowired
	@Mock
	private MessageProducer messageProducerMock;
	
	@Autowired
	@InjectMocks
	private CatalogServiceImpl catalogService;
	
	private Restaurant asianRestaurant;
	private Meal ramen;
	private User bob;
	private Feedback feedback;
	
	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		Mockito.reset(catalogDaoMock);
		Mockito.reset(restaurantDaoMock);
		Mockito.reset(userDaoMock);
		Mockito.reset(messageProducerMock);

		asianRestaurant = new Restaurant();
		asianRestaurant.setName("Lion d'or");
		asianRestaurant.setRestaurantAddress("22 rue des nems");
		
		ramen = new Meal();
		ramen.setName("Ramen soup");
		ramen.addTag(ASIAN_CATEGORY);
		ramen.setRestaurant(asianRestaurant);
		
		bob = new User();
		bob.setFirstName("Bob");
		bob.setLastName("Harington");
		
		feedback = new Feedback();
		feedback.setAuthor(bob);
		feedback.setMeal(ramen);
		feedback.setContent("Trés bon plat");
	}
	
	@AfterEach
	public void cleanUp() throws Exception {
		ramen = null;
	}
	
	@Test
	public void findAMealsByTag () {
		List<Meal> expectedMock = new ArrayList<>();
		expectedMock.add(ramen);
		when(catalogDaoMock.findMealsByTag(anyString())).thenReturn(expectedMock);
		
		List<MealDTO> resultAsDTO = catalogService.findMealsByTag(ASIAN_CATEGORY);
		assertEquals(1, resultAsDTO.size());
	}
	
	@Test
	public void findAMealsByTagWithEmptyTag () {
		List<Meal> expectedMock = new ArrayList<>();
		expectedMock.add(ramen);
		when(catalogDaoMock.listMeals()).thenReturn(expectedMock);
		
		List<MealDTO> resultAsDTO = catalogService.findMealsByTag("");
		assertEquals(1, resultAsDTO.size());
	}
	
	@Test
	public void findMealsForExistingRestaurant() throws Exception {
		List<Meal> expectedMock = new ArrayList<>();
		expectedMock.add(ramen);
		when(restaurantDaoMock.findRestaurantById(anyInt())).thenReturn(Optional.of(asianRestaurant));
		when(catalogDaoMock.findMealsByRestaurant(any(Restaurant.class))).thenReturn(expectedMock);
		
		List<MealDTO> resultAsDTO = catalogService.findMealsByRestaurant(asianRestaurant.getId());
		assertEquals(1, resultAsDTO.size());
	}
	
	@Test
	public void findMealsForNonExistingRestaurant() {
		List<Meal> expectedMock = new ArrayList<>();
		expectedMock.add(ramen);
		when(restaurantDaoMock.findRestaurantById(anyInt())).thenReturn(Optional.empty());
		when(catalogDaoMock.findMealsByRestaurant(any(Restaurant.class))).thenReturn(expectedMock);
		
		assertThrows(UnknowRestaurantException.class, () -> {
			catalogService.findMealsByRestaurant(asianRestaurant.getId());
		});
	}
	
	@Test
	public void addFeedback() throws Exception {
		when(catalogDaoMock.findMealById(anyInt())).thenReturn(Optional.of(ramen));
		when(userDaoMock.findUserById(anyInt())).thenReturn(Optional.of(bob));
		when(catalogDaoMock.addFeedback(any(Feedback.class))).thenReturn(feedback);
		MessageProducer spy = Mockito.spy(messageProducerMock);
		doNothing().when(spy).sendMessage(any(Message.class));
		
		FeedbackDTO feedbackReceived = catalogService.addFeedback(feedback.toDTO(), bob.getId(), ramen.getId());
		assertNotNull(feedbackReceived);
		assertEquals(feedback.getContent(), feedbackReceived.getContent());
	}
	
	@Test
	public void addFeedbackOnNonExistingUser() {
		when(userDaoMock.findUserById(anyInt())).thenReturn(Optional.empty());
		
		assertThrows(UnknowUserException.class, () -> {
			catalogService.addFeedback(feedback.toDTO(), bob.getId(), ramen.getId());
		});
	}
	
	@Test
	public void addFeedbackOnNonExistingMeal() {
		when(catalogDaoMock.findMealById(anyInt())).thenReturn(Optional.empty());
		when(userDaoMock.findUserById(anyInt())).thenReturn(Optional.of(bob));
		
		assertThrows(UnknowMealException.class, () -> {
			catalogService.addFeedback(feedback.toDTO(), bob.getId(), ramen.getId());
		});
	}
}
