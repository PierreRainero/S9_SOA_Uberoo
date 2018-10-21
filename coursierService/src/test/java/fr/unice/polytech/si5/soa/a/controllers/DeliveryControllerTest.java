package fr.unice.polytech.si5.soa.a.controllers;

import fr.unice.polytech.si5.soa.a.communication.DeliveryDTO;
import fr.unice.polytech.si5.soa.a.configuration.TestConfiguration;
import fr.unice.polytech.si5.soa.a.configuration.WebApplicationConfiguration;
import fr.unice.polytech.si5.soa.a.entities.Delivery;
import fr.unice.polytech.si5.soa.a.services.IDeliveryService;
import fr.unice.polytech.si5.soa.a.util.TestUtil;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        TestConfiguration.class,
        WebApplicationConfiguration.class
})
@WebAppConfiguration
public class DeliveryControllerTest {
    private final static String BASE_URI= "/deliveries/";
    private MockMvc  mockMvc;

    @Qualifier("mock")
    @Autowired
    @Mock
    private IDeliveryService deliveryServiceMock;

    @Autowired
    @InjectMocks
    private DeliveryController deliveryController;

    private Delivery delivery;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        Mockito.reset(deliveryServiceMock);
        mockMvc = MockMvcBuilders.standaloneSetup(deliveryController).build();
        delivery = new Delivery();
        delivery.state = false;
    }

    @Test
    public void getDeliveriesToDoTest() throws Exception {
        List<DeliveryDTO> expectedMock = new ArrayList<>();
        expectedMock.add(delivery.toDTO());
        when(deliveryServiceMock.getDeliveriesToDo()).thenReturn(expectedMock);

        mockMvc.perform(get(BASE_URI)
        .contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8));

        verify(deliveryServiceMock,times(1)).getDeliveriesToDo();
        verifyNoMoreInteractions(deliveryServiceMock);
    }
}
