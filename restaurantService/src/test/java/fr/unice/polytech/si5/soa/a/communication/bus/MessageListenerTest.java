package fr.unice.polytech.si5.soa.a.communication.bus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import fr.unice.polytech.si5.soa.a.communication.FeedbackDTO;
import fr.unice.polytech.si5.soa.a.communication.RestaurantOrderDTO;
import fr.unice.polytech.si5.soa.a.communication.bus.messages.NewFeedback;
import fr.unice.polytech.si5.soa.a.communication.bus.messages.NewOrder;
import fr.unice.polytech.si5.soa.a.configuration.TestConfiguration;
import fr.unice.polytech.si5.soa.a.services.IMealService;
import fr.unice.polytech.si5.soa.a.services.IOrderService;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { TestConfiguration.class })
public class MessageListenerTest {
	private static final String FEEDBACK_CONTENT = "Pas mal";
	private static final String MEAL_NAME = "Ramen soup";
	private static final String RESTAURANT_NAME = "RizRiz";
	private static final String RESTAURANT_ADDRESS = "47 avenue des bols";
	
	@Qualifier("mock")
	@Autowired
	@Mock
	private IMealService mealServiceMock;
	
	@Qualifier("mock")
	@Autowired
	@Mock
	private IOrderService orderServiceMock;
	
	@Autowired
    @InjectMocks
    private MessageListener messageListener;
	
	@Captor
    private ArgumentCaptor<ArrayList<String>> listOfStringCaptor;
	
	private NewFeedback newFeedbackMessage;
	private NewOrder newOrderMessage;
	private Date validationDate;
	
	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		Mockito.reset(mealServiceMock);
		Mockito.reset(orderServiceMock);
		
		newFeedbackMessage = new NewFeedback();
		newFeedbackMessage.setAuthor("Bob");
		newFeedbackMessage.setContent(FEEDBACK_CONTENT);
		newFeedbackMessage.setMealName(MEAL_NAME);
		newFeedbackMessage.setRestaurantName(RESTAURANT_NAME);
		newFeedbackMessage.setRestaurantAddress(RESTAURANT_ADDRESS);
		
		List<String> meals = new ArrayList<>();
		meals.add(MEAL_NAME);
		newOrderMessage =  new NewOrder();
		newOrderMessage.setFood(meals);
		newOrderMessage.setRestaurantName(RESTAURANT_NAME);
		newOrderMessage.setRestaurantAddress(RESTAURANT_ADDRESS);
		newOrderMessage.setDate(validationDate);
	}
	
	@Test
	public void handleNewFeedbackMessage() throws Exception {
		when(mealServiceMock.addFeedback(any(FeedbackDTO.class), anyString(), anyString(), anyString())).thenReturn(new FeedbackDTO());
		
		messageListener.listenNewFeedback(newFeedbackMessage);
		
		ArgumentCaptor<FeedbackDTO> feedbackCaptor = ArgumentCaptor.forClass(FeedbackDTO.class);
		ArgumentCaptor<String> mealNameCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<String> restaurantNameCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<String> restaurantAddressCaptor = ArgumentCaptor.forClass(String.class);
		verify(mealServiceMock, times(1)).addFeedback(feedbackCaptor.capture(), mealNameCaptor.capture(), restaurantNameCaptor.capture(), restaurantAddressCaptor.capture());
		
		FeedbackDTO feedbackReceived = feedbackCaptor.getValue();
		assertNotNull(feedbackReceived);
		assertEquals(FEEDBACK_CONTENT, feedbackReceived.getContent());
		
		String mealName = mealNameCaptor.getValue();
		assertEquals(MEAL_NAME, mealName);
		
		String restaurantName = restaurantNameCaptor.getValue();
		assertEquals(RESTAURANT_NAME, restaurantName);
		
		String restaurantAddress = restaurantAddressCaptor.getValue();
		assertEquals(RESTAURANT_ADDRESS, restaurantAddress);
	}
	
	@Test
	public void handleNewOrderMessage() throws Exception {
		when(orderServiceMock.addOrder(any(RestaurantOrderDTO.class), anyList(), anyString(), anyString())).thenReturn(new RestaurantOrderDTO());
		
		messageListener.listenNewOrder(newOrderMessage);
		
		ArgumentCaptor<RestaurantOrderDTO> orderCaptor = ArgumentCaptor.forClass(RestaurantOrderDTO.class);
		ArgumentCaptor<String> restaurantNameCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<String> restaurantAddressCaptor = ArgumentCaptor.forClass(String.class);
		verify(orderServiceMock, times(1)).addOrder(orderCaptor.capture(), listOfStringCaptor.capture(), restaurantNameCaptor.capture(), restaurantAddressCaptor.capture());
		
		RestaurantOrderDTO order = orderCaptor.getValue();
		assertNotNull(order);
		assertEquals(validationDate, order.getValidationDate());
		
		List<String> resultList = listOfStringCaptor.getValue();
		assertNotNull(resultList);
		assertEquals(1, resultList.size());
		
		String restaurantName = restaurantNameCaptor.getValue();
		assertEquals(RESTAURANT_NAME, restaurantName);
		
		String restaurantAddress = restaurantAddressCaptor.getValue();
		assertEquals(RESTAURANT_ADDRESS, restaurantAddress);
	}
}
