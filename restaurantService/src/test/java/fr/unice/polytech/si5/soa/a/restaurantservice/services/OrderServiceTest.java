package fr.unice.polytech.si5.soa.a.restaurantservice.services;
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

import fr.unice.polytech.si5.soa.a.restaurantservice.configuration.TestConfiguration;
import fr.unice.polytech.si5.soa.a.restaurantservice.dao.IOrderDao;
import fr.unice.polytech.si5.soa.a.restaurantservice.model.OrderToPrepare;
import fr.unice.polytech.si5.soa.a.restaurantservice.services.component.OrderServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { TestConfiguration.class })
public class OrderServiceTest {
	@Autowired
	@Qualifier("mock")
	@Mock
	private IOrderDao orderDaoMock;
	
	@Autowired
	@InjectMocks
	private OrderServiceImpl orderService;
	
	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		Mockito.reset(orderDaoMock);
	}
	
	@AfterEach
	public void cleanUp() throws Exception {
		
	}
	
	@Test
	public void getAllOrders () {
		List<OrderToPrepare> expectedMock = new ArrayList<>();
		OrderToPrepare order = new OrderToPrepare();
		expectedMock.add(order);
		when(orderDaoMock.getOrders()).thenReturn(expectedMock);
		
		List<OrderToPrepare> resultAsDTO = orderService.getOrders();
		assertEquals(1, resultAsDTO.size());
	}
}

