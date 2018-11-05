package fr.unice.polytech.si5.soa.a.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

import fr.unice.polytech.si5.soa.a.communication.FeedbackDTO;
import fr.unice.polytech.si5.soa.a.communication.MealDTO;
import fr.unice.polytech.si5.soa.a.communication.RestaurantDTO;
import fr.unice.polytech.si5.soa.a.configuration.TestConfiguration;
import fr.unice.polytech.si5.soa.a.configuration.WebApplicationConfiguration;
import fr.unice.polytech.si5.soa.a.entities.Feedback;
import fr.unice.polytech.si5.soa.a.entities.Meal;
import fr.unice.polytech.si5.soa.a.entities.Restaurant;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowMealException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowRestaurantException;
import fr.unice.polytech.si5.soa.a.services.IMealService;
import fr.unice.polytech.si5.soa.a.services.IRestaurantService;
import fr.unice.polytech.si5.soa.a.util.TestUtil;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfiguration.class, WebApplicationConfiguration.class})
@WebAppConfiguration
public class RestaurantControllerTest {
	private final static String BASE_URI = "/restaurants";
	
	private final static String ERROR_RESTAURANT_NOT_FOUND = "Can't find restaurant with id = 1";
	private final static String ERROR_MEAL_NOT_FOUND = "Can't find meal with id = 1";
	
	private MockMvc mockMvc;
	
	@Qualifier("mock")
	@Autowired
	@Mock
	private IRestaurantService restaurantServiceMock;
	
	@Qualifier("mock")
	@Autowired
	@Mock
	private IMealService mealServiceMock;
	
	@Autowired
    @InjectMocks
    private RestaurantController restaurantController;
	
	private Meal ramen;
	private Feedback feedback;
	private Restaurant asianRestaurant;
	
	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		Mockito.reset(restaurantServiceMock);
		Mockito.reset(mealServiceMock);
		mockMvc = MockMvcBuilders.standaloneSetup(restaurantController).build();
		
		asianRestaurant = new Restaurant();
    	asianRestaurant.setName("RizRiz");
    	asianRestaurant.setRestaurantAddress("47 avenue des bols");
		
		ramen = new Meal();
		ramen.setName("Ramen soup");
        ramen.setPrice(10);
        ramen.setRestaurant(asianRestaurant);
        
        feedback = new Feedback();
		feedback.setAuthor("John");
		feedback.setContent("Bof");
		feedback.setMeal(ramen);
	}
	
	@Test
    public void addARestaurantHTTPPost() throws Exception {
		when(restaurantServiceMock.addRestaurant(any(RestaurantDTO.class))).thenReturn(asianRestaurant.toDTO());

		mockMvc.perform(post(BASE_URI)
               .contentType(TestUtil.APPLICATION_JSON_UTF8)
               .content(TestUtil.convertObjectToJsonBytes(asianRestaurant.toDTO()))
        ).andExpect(status().isOk())
         .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8));
        
        ArgumentCaptor<RestaurantDTO> captor = ArgumentCaptor.forClass(RestaurantDTO.class);
        verify(restaurantServiceMock, times(1)).addRestaurant(captor.capture());
        verifyNoMoreInteractions(restaurantServiceMock);
        
        RestaurantDTO restaurantToAdd = captor.getValue();
        assertNotNull(restaurantToAdd);
        assertEquals(asianRestaurant.getName(), restaurantToAdd.getName());
	}
	
	@Test
    public void addAMealHTTPPost() throws Exception {
		when(mealServiceMock.addMeal(any(MealDTO.class), anyInt())).thenReturn(ramen.toDTO());

		mockMvc.perform(post(BASE_URI+"/1/meals")
               .contentType(TestUtil.APPLICATION_JSON_UTF8)
               .content(TestUtil.convertObjectToJsonBytes(ramen.toDTO()))
        ).andExpect(status().isOk())
         .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8));
        
        ArgumentCaptor<MealDTO> mealCaptor = ArgumentCaptor.forClass(MealDTO.class);
        ArgumentCaptor<Integer> restaurantCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(mealServiceMock, times(1)).addMeal(mealCaptor.capture(), restaurantCaptor.capture());
        verifyNoMoreInteractions(mealServiceMock);
        
        Integer restaurantToAssociated = restaurantCaptor.getValue();
        assertEquals(new Integer(1), restaurantToAssociated);
        
        MealDTO mealToAdd = mealCaptor.getValue();
        assertNotNull(mealToAdd);
        assertEquals(ramen.getName(), mealToAdd.getName());
	}
	
	@Test
	public void addMealOnNonExistingRestaurantHTTPPost() throws Exception {
		when(mealServiceMock.addMeal(any(MealDTO.class), anyInt())).thenThrow(new UnknowRestaurantException(ERROR_RESTAURANT_NOT_FOUND));
		
		mockMvc.perform(post(BASE_URI+"/1/meals")
	               .contentType(TestUtil.APPLICATION_JSON_UTF8)
	               .content(TestUtil.convertObjectToJsonBytes(ramen.toDTO()))
	        ).andExpect(status().isNotFound())
			 .andExpect(content().string(ERROR_RESTAURANT_NOT_FOUND));
		
		ArgumentCaptor<MealDTO> mealCaptor = ArgumentCaptor.forClass(MealDTO.class);
        ArgumentCaptor<Integer> restaurantCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(mealServiceMock, times(1)).addMeal(mealCaptor.capture(), restaurantCaptor.capture());
        verifyNoMoreInteractions(mealServiceMock);
	}
	
	@Test
	public void searchFeedbackForAMealUsingHTTPGet() throws Exception {
		List<FeedbackDTO> resultMocked = new ArrayList<>();
		resultMocked.add(feedback.toDTO());
		when(mealServiceMock.findFeedbackForMeal(anyInt())).thenReturn(resultMocked);
		
		mockMvc.perform(get(BASE_URI+"/1/meals/2/feedbacks")
	               .contentType(TestUtil.APPLICATION_JSON_UTF8)
	        ).andExpect(status().isOk())
	         .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8));
		
		 ArgumentCaptor<Integer> mealIdCaptor = ArgumentCaptor.forClass(Integer.class);
	     verify(mealServiceMock, times(1)).findFeedbackForMeal(mealIdCaptor.capture());
	     verifyNoMoreInteractions(mealServiceMock);
	     
	     Integer mealId = mealIdCaptor.getValue();
	     assertEquals(new Integer(2), mealId);
	}

	@Test
	public void getAllRestaurantsUsingHTTPGet() throws Exception {
		List<RestaurantDTO> resultMocked = new ArrayList<>();
		resultMocked.add(asianRestaurant.toDTO());
		when(restaurantServiceMock.getAllRestaurants()).thenReturn(resultMocked);

		mockMvc.perform(get(BASE_URI+"/")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
		).andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8));
	}
	
	@Test
	public void searchFeedbackForNonExistingMealUsingHTTPPost() throws Exception {
		when(mealServiceMock.findFeedbackForMeal(anyInt())).thenThrow(new UnknowMealException(ERROR_MEAL_NOT_FOUND));
		
		mockMvc.perform(get(BASE_URI+"/1/meals/2/feedbacks")
	               .contentType(TestUtil.APPLICATION_JSON_UTF8)
	        ).andExpect(status().isNotFound())
		     .andExpect(content().string(ERROR_MEAL_NOT_FOUND));
	}
}
