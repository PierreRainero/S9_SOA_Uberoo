package fr.unice.polytech.si5.soa.a.communication.bus;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

import fr.unice.polytech.si5.soa.a.communication.MealDTO;
import fr.unice.polytech.si5.soa.a.communication.PaymentDTO;
import fr.unice.polytech.si5.soa.a.communication.RestaurantDTO;
import fr.unice.polytech.si5.soa.a.communication.bus.messages.NewMeal;
import fr.unice.polytech.si5.soa.a.communication.bus.messages.NewRestaurant;
import fr.unice.polytech.si5.soa.a.communication.bus.messages.PaymentConfirmation;
import fr.unice.polytech.si5.soa.a.configuration.TestConfiguration;
import fr.unice.polytech.si5.soa.a.entities.states.PaymentState;
import fr.unice.polytech.si5.soa.a.services.IPaymentService;
import fr.unice.polytech.si5.soa.a.services.IRestaurantService;

/**
 * Class name	MessageListenerTest
 * Date			28/10/2018
 * @author		PierreRainero
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { TestConfiguration.class })
public class MessageListenerTest {
	@Qualifier("mock")
	@Autowired
	@Mock
    private IRestaurantService restaurantServiceMock;
	
	@Qualifier("mock")
	@Autowired
	@Mock
	private IPaymentService paymentServiceMock;
	
	@Autowired
    @InjectMocks
    private MessageListener messageListener;
	
	PaymentConfirmation paymentConfirmationMessage;
	NewRestaurant newRestaurantMessage;
	NewMeal newMealMessage;
	
	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		Mockito.reset(restaurantServiceMock);
		Mockito.reset(paymentServiceMock);
		
		paymentConfirmationMessage = new PaymentConfirmation();
		paymentConfirmationMessage.setId(-1);
		paymentConfirmationMessage.setStatus(true);
		
		newRestaurantMessage = new NewRestaurant();
		newRestaurantMessage.setName("Lion d'or");
		newRestaurantMessage.setAddress("22 rue des nems");
		
		newMealMessage = new NewMeal();
		newMealMessage.setName("Ramen soup");
		newMealMessage.setPrice(10);
		newMealMessage.setRestaurantName("Lion d'or");
		newMealMessage.setRestaurantAddress("22 rue des nems");
	}
	
	@Test
	public void handleNewMealMessage() throws Exception {
		when(restaurantServiceMock.addMeal(any(MealDTO.class), anyString(), anyString())).thenReturn(new MealDTO());
		messageListener.listenNewMeal(newMealMessage);
		
		ArgumentCaptor<MealDTO> mealCaptor = ArgumentCaptor.forClass(MealDTO.class);
		ArgumentCaptor<String> restaurantNameCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<String> restaurantAddresscaptor = ArgumentCaptor.forClass(String.class);
		verify(restaurantServiceMock, times(1)).addMeal(mealCaptor.capture(), restaurantNameCaptor.capture(), restaurantAddresscaptor.capture());
		
		MealDTO meal = mealCaptor.getValue();
		assertNotNull(meal);
		assertEquals(newMealMessage.getName(), meal.getName());
		
		String restaurantName = restaurantNameCaptor.getValue();
		assertNotNull(restaurantName);
		assertEquals(newMealMessage.getRestaurantName(), restaurantName);
		
		String restaurantAddress = restaurantAddresscaptor.getValue();
		assertNotNull(restaurantAddress);
		assertEquals(newMealMessage.getRestaurantAddress(), restaurantAddress);
	}
	
	@Test
	public void handleNewRestaurantMessage() throws Exception {
		when(restaurantServiceMock.addRestaurant(any(RestaurantDTO.class))).thenReturn(new RestaurantDTO());
		messageListener.listenNewRestaurant(newRestaurantMessage);
		
		ArgumentCaptor<RestaurantDTO> restaurantCaptor = ArgumentCaptor.forClass(RestaurantDTO.class);
		verify(restaurantServiceMock, times(1)).addRestaurant(restaurantCaptor.capture());
		
		RestaurantDTO restaurant = restaurantCaptor.getValue();
		assertNotNull(restaurant);
		assertEquals(newRestaurantMessage.getName(), restaurant.getName());
		assertEquals(newRestaurantMessage.getAddress(), restaurant.getRestaurantAddress());
	}
	
	@Test
	public void handleAcceptedPaymentMessage() throws Exception {
		when(paymentServiceMock.updatePaymentStatus(anyInt(), any(PaymentState.class))).thenReturn(new PaymentDTO());
		messageListener.listenPaymentConfirmation(paymentConfirmationMessage);
		
		ArgumentCaptor<Integer> paymentIdCaptor = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<PaymentState> paymentStateCaptor = ArgumentCaptor.forClass(PaymentState.class);
		verify(paymentServiceMock, times(1)).updatePaymentStatus(paymentIdCaptor.capture(), paymentStateCaptor.capture());
		
		Integer paymentId = paymentIdCaptor.getValue();
		assertEquals(new Integer(paymentConfirmationMessage.getId()), paymentId);
		
		PaymentState paymentState = paymentStateCaptor.getValue();
		assertEquals(PaymentState.ACCEPTED, paymentState);
	}
	
	@Test
	public void handleRefusedPaymentMessage() throws Exception {
		when(paymentServiceMock.updatePaymentStatus(anyInt(), any(PaymentState.class))).thenReturn(new PaymentDTO());
		paymentConfirmationMessage.setStatus(false);
		messageListener.listenPaymentConfirmation(paymentConfirmationMessage);
		
		ArgumentCaptor<Integer> paymentIdCaptor = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<PaymentState> paymentStateCaptor = ArgumentCaptor.forClass(PaymentState.class);
		verify(paymentServiceMock, times(1)).updatePaymentStatus(paymentIdCaptor.capture(), paymentStateCaptor.capture());
		
		Integer paymentId = paymentIdCaptor.getValue();
		assertEquals(new Integer(paymentConfirmationMessage.getId()), paymentId);
		
		PaymentState paymentState = paymentStateCaptor.getValue();
		assertEquals(PaymentState.REFUSED, paymentState);
	}
}
