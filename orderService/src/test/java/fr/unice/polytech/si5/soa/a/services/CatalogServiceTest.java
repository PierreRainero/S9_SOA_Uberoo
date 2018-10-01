package fr.unice.polytech.si5.soa.a.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

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

import fr.unice.polytech.si5.soa.a.communication.MealDTO;
import fr.unice.polytech.si5.soa.a.configuration.TestConfiguration;
import fr.unice.polytech.si5.soa.a.dao.ICatalogDao;
import fr.unice.polytech.si5.soa.a.entities.Meal;

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
	@InjectMocks
	private ICatalogService catalogService;
	
	private Meal ramen;
	
	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		Mockito.reset(catalogDaoMock);

		ramen = new Meal();
		ramen.setName("Ramen soup");
		ramen.addTag(ASIAN_CATEGORY);
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
}
