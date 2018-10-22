package fr.unice.polytech.si5.soa.a.controllers;

import fr.unice.polytech.si5.soa.a.communication.*;


import fr.unice.polytech.si5.soa.a.configuration.TestConfiguration;
import fr.unice.polytech.si5.soa.a.configuration.WebApplicationConfiguration;
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
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;


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
        MealDTO ramen = new MealDTO(-1, "Ramen", 10, new ArrayList<IngredientDTO>());
        asianOrder.getMeals().add(ramen);
    }
}