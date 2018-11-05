package fr.unice.polytech.si5.soa.a.controllers;

import fr.unice.polytech.si5.soa.a.communication.*;


import fr.unice.polytech.si5.soa.a.configuration.TestConfiguration;
import fr.unice.polytech.si5.soa.a.configuration.WebApplicationConfiguration;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowOrderException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowRestaurantException;
import fr.unice.polytech.si5.soa.a.services.IOrderService;
import fr.unice.polytech.si5.soa.a.util.TestUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;


import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfiguration.class, WebApplicationConfiguration.class})
@WebAppConfiguration
class OrderControllerTest {
    private final static String BASE_URI = "/restaurants";
    
    private final static String ERROR_RESTAURANT_NOT_FOUND = "Can't find restaurant with id = 1";
    private final static String ERROR_BAD_REQUEST = "Incorrect request according to the URI";
    private final static String ERROR_ORDER_NOT_FOUND = "Can't find order with id = 1";
    private MockMvc mockMvc;

    @Qualifier("mock")
    @Autowired
    @Mock
    private IOrderService orderServiceMock;
    
    @Autowired
    @InjectMocks
    private OrderController orderController;

    private RestaurantOrderDTO asianOrder;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        Mockito.reset(orderServiceMock);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
        
        RestaurantDTO restaurant = new RestaurantDTO();
        restaurant.setName("RizRiz");
        restaurant.setRestaurantAddress("47 avenue des bols");

        asianOrder = new RestaurantOrderDTO();
        asianOrder.setRestaurant(restaurant);
    }
    
    @Test
    public void getOrdersToDoUsingHTTPGet() throws Exception {
    	List<RestaurantOrderDTO> expectedMock = new ArrayList<>();
		expectedMock.add(asianOrder);
		when(orderServiceMock.getOrdersToDo(anyInt())).thenReturn(expectedMock);
		
		mockMvc.perform(get(BASE_URI+"/1/orders")
               .contentType(TestUtil.APPLICATION_JSON_UTF8)
        ).andExpect(status().isOk())
         .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8));
		
		ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
        verify(orderServiceMock, times(1)).getOrdersToDo(captor.capture());
        verifyNoMoreInteractions(orderServiceMock);
        
        Integer restaurantId = captor.getValue();
        assertEquals(new Integer(1), restaurantId);
    }
    
    @Test
    public void getOrdersToDoWithUnknowRestaurantUsingHTTPGet() throws Exception {
		when(orderServiceMock.getOrdersToDo(anyInt())).thenThrow(new UnknowRestaurantException(ERROR_RESTAURANT_NOT_FOUND));
		
		mockMvc.perform(get(BASE_URI+"/1/orders")
               .contentType(TestUtil.APPLICATION_JSON_UTF8)
        ).andExpect(status().isNotFound())
		 .andExpect(content().string(ERROR_RESTAURANT_NOT_FOUND));
		
		ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
        verify(orderServiceMock, times(1)).getOrdersToDo(captor.capture());
        verifyNoMoreInteractions(orderServiceMock);
    }
    
    @Test
    public void updateAnOrderUsingHTTPPUt() throws Exception {
    	asianOrder.getRestaurant().setId(1);
    	asianOrder.setId(2);
		when(orderServiceMock.updateOrder(any(RestaurantOrderDTO.class))).thenReturn(asianOrder);
		
		mockMvc.perform(put(BASE_URI+"/1/orders/2/")
               .contentType(TestUtil.APPLICATION_JSON_UTF8)
               .content(TestUtil.convertObjectToJsonBytes(asianOrder))
        ).andExpect(status().isOk())
         .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8));
		
		ArgumentCaptor<RestaurantOrderDTO> captor = ArgumentCaptor.forClass(RestaurantOrderDTO.class);
        verify(orderServiceMock, times(1)).updateOrder(captor.capture());
        verifyNoMoreInteractions(orderServiceMock);
        
        RestaurantOrderDTO restaurantToUpdate = captor.getValue();
        assertEquals(asianOrder.getMeals().size(), restaurantToUpdate.getMeals().size());
    }
    
    @Test
    public void updateAnOrderWithIncorrectOrderUsingHTTPPUt() throws Exception {
    	asianOrder.getRestaurant().setId(1);
    	asianOrder.setId(1);
		
		mockMvc.perform(put(BASE_URI+"/1/orders/2/")
               .contentType(TestUtil.APPLICATION_JSON_UTF8)
               .content(TestUtil.convertObjectToJsonBytes(asianOrder))
        ).andExpect(status().isBadRequest())
		 .andExpect(content().string(ERROR_BAD_REQUEST));
    }
    
    @Test
    public void updateAnOrderWithIncorrectRestaurantUsingHTTPPUt() throws Exception {
    	asianOrder.getRestaurant().setId(2);
    	asianOrder.setId(2);
		
		mockMvc.perform(put(BASE_URI+"/1/orders/2/")
               .contentType(TestUtil.APPLICATION_JSON_UTF8)
               .content(TestUtil.convertObjectToJsonBytes(asianOrder))
        ).andExpect(status().isBadRequest())
		 .andExpect(content().string(ERROR_BAD_REQUEST));
    }
    
    @Test
    public void updateAnOrderWithUnknowOrderUsingHTTPPUt() throws Exception {
    	asianOrder.getRestaurant().setId(1);
    	asianOrder.setId(2);
		when(orderServiceMock.updateOrder(any(RestaurantOrderDTO.class))).thenThrow(new UnknowOrderException(ERROR_ORDER_NOT_FOUND));
		
		mockMvc.perform(put(BASE_URI+"/1/orders/2/")
               .contentType(TestUtil.APPLICATION_JSON_UTF8)
               .content(TestUtil.convertObjectToJsonBytes(asianOrder))
        ).andExpect(status().isNotFound())
		 .andExpect(content().string(ERROR_ORDER_NOT_FOUND));
    }
}