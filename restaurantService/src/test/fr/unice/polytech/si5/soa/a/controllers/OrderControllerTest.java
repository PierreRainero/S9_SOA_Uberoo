package fr.unice.polytech.si5.soa.a.controllers;

import fr.unice.polytech.si5.soa.a.communication.Message;
import fr.unice.polytech.si5.soa.a.communication.NewOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.unice.polytech.si5.soa.a.communication.RestaurantOrderDTO;
import fr.unice.polytech.si5.soa.a.configuration.TestConfiguration;
import fr.unice.polytech.si5.soa.a.configuration.WebApplicationConfiguration;
import fr.unice.polytech.si5.soa.a.entities.Ingredient;
import fr.unice.polytech.si5.soa.a.entities.Meal;
import fr.unice.polytech.si5.soa.a.services.IOrderService;
import fr.unice.polytech.si5.soa.a.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;


import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfiguration.class, WebApplicationConfiguration.class})
@WebAppConfiguration
class OrderControllerTest {
    private final static String BASE_URI = "/restaurants/orders";

    private MockMvc mockMvc;

    @Qualifier("mock")
    @Autowired
    @Mock
    private IOrderService orderServiceMock;

    private RestaurantOrderDTO testOrder;

    private RestaurantOrderDTO asianOrder;

    @Autowired
    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        Mockito.reset(orderServiceMock);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();

        asianOrder = new RestaurantOrderDTO();
        List<Meal> meals = asianOrder.getMeals();
        meals.add(new Meal(new Ingredient("Ramen", 6)));
    }


    @Test
    void addOrder() throws Exception {
        when(orderServiceMock.addOrder(any(RestaurantOrderDTO.class))).thenReturn(asianOrder);
        NewOrder asianOrderMsg = new NewOrder();
        asianOrderMsg.setType("NEW_ORDER");
        asianOrderMsg.setAddress("");
        List<Meal> meals = new ArrayList<>();
        meals.add(new Meal(new Ingredient("Ramen")));
        asianOrderMsg.setMeals(meals);

        mockMvc.perform(post(BASE_URI)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(asianOrderMsg))
        ).andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8));

        ArgumentCaptor<RestaurantOrderDTO> captor = ArgumentCaptor.forClass(RestaurantOrderDTO.class);
        verify(orderServiceMock, times(1)).addOrder(captor.capture());
        verifyNoMoreInteractions(orderServiceMock);

        RestaurantOrderDTO transmittedOrder = captor.getValue();
        assertNotNull(transmittedOrder);
        assertEquals(1, transmittedOrder.getMeals().size());
        assertEquals("Ramen", transmittedOrder.getMeals().get(0).getIngredients().get(0).getName());
    }

    @Test
    void updateOrderState() {
    }

    @Test
    void getDeliveriesToDo() {
    }
}