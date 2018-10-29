package fr.unice.polytech.si5.soa.a.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
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

import fr.unice.polytech.si5.soa.a.communication.MealDTO;
import fr.unice.polytech.si5.soa.a.communication.FeedbackDTO;
import fr.unice.polytech.si5.soa.a.configuration.TestConfiguration;
import fr.unice.polytech.si5.soa.a.configuration.WebApplicationConfiguration;
import fr.unice.polytech.si5.soa.a.entities.Feedback;
import fr.unice.polytech.si5.soa.a.entities.Meal;
import fr.unice.polytech.si5.soa.a.entities.Restaurant;
import fr.unice.polytech.si5.soa.a.entities.User;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowMealException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowRestaurantException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowUserException;
import fr.unice.polytech.si5.soa.a.services.ICatalogService;
import fr.unice.polytech.si5.soa.a.util.TestUtil;

/**
 * Class name	CatalogControllerTest
 * Date			01/10/2018
 * @author		PierreRainero
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfiguration.class, WebApplicationConfiguration.class})
@WebAppConfiguration
public class CatalogControllerTest {
	private final static String BASE_URI = "/meals/";
	private final static String ERROR_UNKNOW_RESTAURANT = "Can't find restaurant with id = -1";
	private final static String ERROR_USER_RESTAURANT ="Can't find user with id = -1";
	private final static String ERROR_MEAL_RESTAURANT ="Can't find meal with id = -1";
	
	private static final String ASIAN_CATEGORY = "Asian";
	private MockMvc mockMvc;
	
	@Qualifier("mock")
	@Autowired
	@Mock
    private ICatalogService catalogServiceMock;
	
	@Autowired
    @InjectMocks
    private CatalogController catalogController;
	
	private Restaurant asianRestaurant;
	private Meal ramen;
	private User bob;
	private Feedback feedback;
	
    @BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		Mockito.reset(catalogServiceMock);
		mockMvc = MockMvcBuilders.standaloneSetup(catalogController).build();
		
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
    
    @Test
    public void searchAsianMealHTTPGet() throws Exception {
    	List<MealDTO> expectedMock = new ArrayList<>();
		expectedMock.add(ramen.toDTO());
		when(catalogServiceMock.findMealsByTag(anyString())).thenReturn(expectedMock);
		
		mockMvc.perform(get(BASE_URI)
               .contentType(TestUtil.APPLICATION_JSON_UTF8)
               .param("tag", ASIAN_CATEGORY)
        ).andExpect(status().isOk())
         .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8));
        
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(catalogServiceMock, times(1)).findMealsByTag(captor.capture());
        verifyNoMoreInteractions(catalogServiceMock);
        
        String tagToUse = captor.getValue();
        assertEquals(ASIAN_CATEGORY, tagToUse);
	}
    
    @Test
    public void searchMealWithoutTagHTTPGet() throws Exception {
    	List<MealDTO> expectedMock = new ArrayList<>();
		expectedMock.add(ramen.toDTO());
		when(catalogServiceMock.findMealsByTag(anyString())).thenReturn(expectedMock);
		
		mockMvc.perform(get(BASE_URI)
               .contentType(TestUtil.APPLICATION_JSON_UTF8)
        ).andExpect(status().isOk())
         .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8));
		
		ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(catalogServiceMock, times(1)).findMealsByTag(captor.capture());
        verifyNoMoreInteractions(catalogServiceMock);
        
        String tagToUse = captor.getValue();
        assertTrue(tagToUse.isEmpty());
	}
    
    @Test
    public void searchMealsOfAsianRestaurantHTTPGet() throws Exception {
    	List<MealDTO> expectedMock = new ArrayList<>();
		expectedMock.add(ramen.toDTO());
		when(catalogServiceMock.findMealsByRestaurant(anyInt())).thenReturn(expectedMock);
		
		mockMvc.perform(get("/restaurants/-1"+BASE_URI)
               .contentType(TestUtil.APPLICATION_JSON_UTF8)
        ).andExpect(status().isOk())
         .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8));
        
        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
        verify(catalogServiceMock, times(1)).findMealsByRestaurant(captor.capture());
        verifyNoMoreInteractions(catalogServiceMock);
        
        Integer idToUse = captor.getValue();
        assertEquals(new Integer(-1), idToUse);
	}
    
    @Test
    public void searchMealsOfNonExistingRestaurantHTTPGet() throws Exception {
    	List<MealDTO> expectedMock = new ArrayList<>();
		expectedMock.add(ramen.toDTO());
		when(catalogServiceMock.findMealsByRestaurant(anyInt())).thenThrow(new UnknowRestaurantException(ERROR_UNKNOW_RESTAURANT));
		
		mockMvc.perform(get("/restaurants/-1"+BASE_URI)
               .contentType(TestUtil.APPLICATION_JSON_UTF8)
        ).andExpect(status().isNotFound())
		 .andExpect(content().string(ERROR_UNKNOW_RESTAURANT));
        
        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
        verify(catalogServiceMock, times(1)).findMealsByRestaurant(captor.capture());
        verifyNoMoreInteractions(catalogServiceMock);
	}
    
    @Test
    public void addFeedbackUsingHTTPPost() throws Exception {
		when(catalogServiceMock.addFeedback(any(FeedbackDTO.class), anyInt(), anyInt())).thenReturn(feedback.toDTO());

		mockMvc.perform(post(BASE_URI+"/1/feedbacks")
               .contentType(TestUtil.APPLICATION_JSON_UTF8)
               .content(TestUtil.convertObjectToJsonBytes(feedback.toDTO()))
        ).andExpect(status().isOk())
         .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8));
        
		ArgumentCaptor<FeedbackDTO> feedbackCaptor = ArgumentCaptor.forClass(FeedbackDTO.class);
		ArgumentCaptor<Integer> authorIdCaptor = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<Integer> mealIdCaptor = ArgumentCaptor.forClass(Integer.class);
		verify(catalogServiceMock, times(1)).addFeedback(feedbackCaptor.capture(), authorIdCaptor.capture(), mealIdCaptor.capture());
        verifyNoMoreInteractions(catalogServiceMock);
        
        FeedbackDTO feedbackSent = feedbackCaptor.getValue();
        assertNotNull(feedbackSent);
        assertEquals(feedback.getContent(), feedbackSent.getContent());
        
        Integer mealId = mealIdCaptor.getValue();
        assertEquals(new Integer(1), mealId);
        
        Integer authorId = authorIdCaptor.getValue();
        assertEquals(new Integer(0), authorId);
	}
    
    @Test
    public void addMalformedFeedbackUsingHTTPPost() throws Exception {
    	when(catalogServiceMock.addFeedback(any(FeedbackDTO.class), anyInt(), anyInt())).thenReturn(feedback.toDTO());
    	FeedbackDTO data =feedback.toDTO();
    	data.setAuthor(null);

		mockMvc.perform(post(BASE_URI+"/1/feedbacks")
               .contentType(TestUtil.APPLICATION_JSON_UTF8)
               .content(TestUtil.convertObjectToJsonBytes(data))
        ).andExpect(status().isBadRequest())
		 .andExpect(content().string("Malformed request."));
    }
    
    @Test
    public void addFeedbackWithUnkownMealUsingHTTPPost() throws Exception {
    	when(catalogServiceMock.addFeedback(any(FeedbackDTO.class), anyInt(), anyInt())).thenThrow(new UnknowMealException(ERROR_MEAL_RESTAURANT));

		mockMvc.perform(post(BASE_URI+"/1/feedbacks")
               .contentType(TestUtil.APPLICATION_JSON_UTF8)
               .content(TestUtil.convertObjectToJsonBytes(feedback.toDTO()))
        ).andExpect(status().isNotFound())
		 .andExpect(content().string(ERROR_MEAL_RESTAURANT));
    }
    
    @Test
    public void addFeedbackWithUnkownUserUsingHTTPPost() throws Exception {
    	when(catalogServiceMock.addFeedback(any(FeedbackDTO.class), anyInt(), anyInt())).thenThrow(new UnknowUserException(ERROR_USER_RESTAURANT));

		mockMvc.perform(post(BASE_URI+"/1/feedbacks")
               .contentType(TestUtil.APPLICATION_JSON_UTF8)
               .content(TestUtil.convertObjectToJsonBytes(feedback.toDTO()))
        ).andExpect(status().isNotFound())
		 .andExpect(content().string(ERROR_USER_RESTAURANT));
    }
}
