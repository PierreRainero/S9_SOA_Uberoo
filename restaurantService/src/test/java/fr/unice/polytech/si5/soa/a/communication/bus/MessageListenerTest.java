package fr.unice.polytech.si5.soa.a.communication.bus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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

import fr.unice.polytech.si5.soa.a.communication.FeedbackDTO;
import fr.unice.polytech.si5.soa.a.communication.bus.messages.NewFeedback;
import fr.unice.polytech.si5.soa.a.configuration.TestConfiguration;
import fr.unice.polytech.si5.soa.a.services.IMealService;

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
	private IMealService mealService;
	
	@Autowired
    @InjectMocks
    private MessageListener messageListener;
	
	private NewFeedback newFeedbackMessage;
	
	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		Mockito.reset(mealService);
		
		newFeedbackMessage = new NewFeedback();
		newFeedbackMessage.setAuthor("Bob");
		newFeedbackMessage.setContent(FEEDBACK_CONTENT);
		newFeedbackMessage.setMealName(MEAL_NAME);
		newFeedbackMessage.setRestaurantName(RESTAURANT_NAME);
		newFeedbackMessage.setRestaurantAddress(RESTAURANT_ADDRESS);
	}
	
	@Test
	public void handleNewFeedbackMessage() throws Exception {
		when(mealService.addFeedback(any(FeedbackDTO.class), anyString(), anyString(), anyString())).thenReturn(new FeedbackDTO());
		messageListener.listenNewFeedback(newFeedbackMessage);
		
		ArgumentCaptor<FeedbackDTO> feedbackCaptor = ArgumentCaptor.forClass(FeedbackDTO.class);
		ArgumentCaptor<String> mealNameCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<String> restaurantNameCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<String> restaurantAddressCaptor = ArgumentCaptor.forClass(String.class);
		verify(mealService, times(1)).addFeedback(feedbackCaptor.capture(), mealNameCaptor.capture(), restaurantNameCaptor.capture(), restaurantAddressCaptor.capture());
		
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
}
