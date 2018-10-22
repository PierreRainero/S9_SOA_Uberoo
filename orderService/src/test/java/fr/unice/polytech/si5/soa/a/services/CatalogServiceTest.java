package fr.unice.polytech.si5.soa.a.services;

import fr.unice.polytech.si5.soa.a.communication.MealDTO;
import fr.unice.polytech.si5.soa.a.configuration.TestConfiguration;
import fr.unice.polytech.si5.soa.a.dao.ICatalogDao;
import fr.unice.polytech.si5.soa.a.dao.IRestaurantDao;
import fr.unice.polytech.si5.soa.a.entities.Meal;
import fr.unice.polytech.si5.soa.a.entities.Restaurant;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowRestaurantException;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyInt;
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
	private IRestaurantDao restaurantDaoMock;
	
	@Autowired
	@InjectMocks
	private CatalogServiceImpl catalogService;
	
	private Restaurant asianRestaurant;
	private Meal ramen;
	
	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		Mockito.reset(catalogDaoMock);
		Mockito.reset(restaurantDaoMock);

		asianRestaurant = new Restaurant();
		asianRestaurant.setName("Lion d'or");
		asianRestaurant.setRestaurantAddress("22 rue des nems");
		
		ramen = new Meal();
		ramen.setName("Ramen soup");
		ramen.addTag(ASIAN_CATEGORY);
		ramen.setRestaurant(asianRestaurant);
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
}
