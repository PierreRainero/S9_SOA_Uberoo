package fr.unice.polytech.si5.soa.a.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import fr.unice.polytech.si5.soa.a.configuration.TestConfiguration;
import fr.unice.polytech.si5.soa.a.configuration.WebApplicationConfiguration;
import fr.unice.polytech.si5.soa.a.entities.Meal;
import fr.unice.polytech.si5.soa.a.entities.Restaurant;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowRestaurantException;
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
}
