package fr.unice.polytech.si5.soa.a.controllers;

import fr.unice.polytech.si5.soa.a.communication.DTO.CancelDataDTO;
import fr.unice.polytech.si5.soa.a.communication.DTO.DeliveryDTO;
import fr.unice.polytech.si5.soa.a.communication.DTO.RestaurantDTO;
import fr.unice.polytech.si5.soa.a.communication.message.NewOrder;
import fr.unice.polytech.si5.soa.a.configuration.TestConfiguration;
import fr.unice.polytech.si5.soa.a.configuration.WebApplicationConfiguration;
import fr.unice.polytech.si5.soa.a.entities.Coursier;
import fr.unice.polytech.si5.soa.a.entities.Delivery;
import fr.unice.polytech.si5.soa.a.exceptions.UnknownCoursierException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknownDeliveryException;
import fr.unice.polytech.si5.soa.a.services.IDeliveryService;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        TestConfiguration.class,
        WebApplicationConfiguration.class
})
@WebAppConfiguration
public class DeliveryControllerTest {
    private final static String BASE_URI = "/deliveries/";
    private MockMvc mockMvc;

    @Qualifier("mock")
    @Autowired
    @Mock
    private IDeliveryService deliveryServiceMock;

    @Autowired
    @InjectMocks
    private DeliveryController deliveryController;

    private Delivery delivery;
    private Delivery deliveryDone;
    private Delivery deliveryBelow10;
    private Delivery deliveryOver10;
    private NewOrder order;
    private Coursier coursier;

    private static final String ADDRESS = "475 rue Evariste Galois";
    private static final String ERROR_MESSAGE = "Can't find delivery";


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Mockito.reset(deliveryServiceMock);

        coursier = new Coursier();
        coursier.setName("Jean");
        coursier.setAccountNumber("FR XXXX XXXXX");
        coursier.setId(5);

        mockMvc = MockMvcBuilders.standaloneSetup(deliveryController).build();

        delivery = new Delivery();
        delivery.setDeliveryAddress(ADDRESS);
        delivery.setId(8);
        delivery.setState(false);
        delivery.setCoursierGetPaid(false);
        delivery.setCoursier(coursier);

        String restaurantName = "Macdonald";
        String restaurantAddress = "4 rue de la malbouffe";

        RestaurantDTO restaurantDTO = new RestaurantDTO(restaurantName, restaurantAddress);


        delivery.setRestaurant(restaurantDTO.createRestaurant());

        deliveryDone = new Delivery();
        deliveryDone.setDeliveryAddress(ADDRESS);
        deliveryDone.setState(true);
        deliveryDone.setCoursier(coursier);

        deliveryBelow10 = new Delivery();
        deliveryBelow10.setDeliveryAddress(ADDRESS);
        deliveryBelow10.setState(false);
        deliveryBelow10.setLatitude(0.0);
        deliveryBelow10.setLongitude(0.0);
        deliveryBelow10.setCoursier(coursier);

        deliveryOver10 = new Delivery();
        deliveryOver10.setDeliveryAddress(ADDRESS);
        deliveryOver10.setState(false);
        deliveryOver10.setLatitude(1.0);
        deliveryOver10.setLongitude(1.0);
        deliveryOver10.setCoursier(coursier);

        order = new NewOrder();
        order.setRestaurantName(restaurantName);
        order.setRestaurantAddress(restaurantAddress);
        order.setAddress(ADDRESS);
        order.setFood(Arrays.asList("Sushi", "Maki"));
        order.setType("Sushi");
    }

    @Test
    public void getDeliveriesToDoTest() throws Exception {
        List<DeliveryDTO> expectedMock = new ArrayList<>();
        expectedMock.add(delivery.toDTO());
        when(deliveryServiceMock.getDeliveriesToDo()).thenReturn(expectedMock);

        mockMvc.perform(get(BASE_URI)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(expectedMock)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8));
    }

    @Test
    public void addDeliveryTest() throws Exception {
        when(deliveryServiceMock.addDelivery(any(DeliveryDTO.class))).thenReturn(delivery.toDTO());
        mockMvc.perform(post(BASE_URI)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(order)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8));

        ArgumentCaptor<DeliveryDTO> captor = ArgumentCaptor.forClass(DeliveryDTO.class);
        verify(deliveryServiceMock, times(1)).addDelivery(captor.capture());
        verifyNoMoreInteractions(deliveryServiceMock);

        DeliveryDTO captorValue = captor.getValue();
        assertNotNull(captorValue);
        assertEquals(captorValue, delivery.toDTO());
    }

    @Test
    public void updateDeliveryStateCorrectly() throws Exception {
        when(deliveryServiceMock.updateDelivery(any(DeliveryDTO.class))).thenReturn(deliveryDone.toDTO());

        mockMvc.perform(put(BASE_URI)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(deliveryDone.toDTO())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8));

        ArgumentCaptor<DeliveryDTO> captor = ArgumentCaptor.forClass(DeliveryDTO.class);
        verify(deliveryServiceMock, times(1)).updateDelivery(captor.capture());
        verifyNoMoreInteractions(deliveryServiceMock);

        DeliveryDTO deliveryDTO = captor.getValue();
        assertNotNull(deliveryDTO);
        assertEquals(deliveryDTO, deliveryDone.toDTO());
    }

    @Test
    public void tryUpdateDeliveryStateCantFindDelivery() throws Exception {
        when(deliveryServiceMock.updateDelivery(any(DeliveryDTO.class))).thenThrow(new UnknownDeliveryException(ERROR_MESSAGE));

        mockMvc.perform(put(BASE_URI)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(new Delivery().toDTO())))
                .andExpect(status().isNotFound())
                .andExpect(content().string(ERROR_MESSAGE));

        ArgumentCaptor<DeliveryDTO> captor = ArgumentCaptor.forClass(DeliveryDTO.class);
        verify(deliveryServiceMock, times(1)).updateDelivery(captor.capture());
        verifyNoMoreInteractions(deliveryServiceMock);
    }

    @Test
    public void getDeliveriesToDoWithPositionTest() throws Exception {
        List<DeliveryDTO> expectedMock = new ArrayList<>();
        expectedMock.add(deliveryBelow10.toDTO());
        when(deliveryServiceMock.getDeliveriesToDo()).thenReturn(expectedMock);

        mockMvc.perform(get(BASE_URI + "?latitude=0.05&longitude=0.5")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(expectedMock)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8));
    }

    @Test
    public void assignDeliveryTest() throws Exception {
        DeliveryDTO expectedMock = delivery.toDTO();
        expectedMock.setCreationDate(new Date());
        expectedMock.setCoursier(this.coursier.toDto());
        when(deliveryServiceMock.assignDelivery(this.delivery.getId(), this.coursier.getId())).thenReturn(expectedMock);
        mockMvc.perform(put(BASE_URI + delivery.getId() + "/?coursierId=" + coursier.getId())
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(expectedMock)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8));

        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> captor2 = ArgumentCaptor.forClass(Integer.class);
        verify(deliveryServiceMock, times(1)).assignDelivery(captor.capture(), captor2.capture());
        verifyNoMoreInteractions(deliveryServiceMock);
        assertEquals(captor.getValue(), delivery.getId());
        assertEquals(captor2.getValue(), coursier.getId());
    }

    @Test
    public void replaceOrderTest() throws Exception {
        DeliveryDTO expectedMock = delivery.toDTO();
        expectedMock.setCancel(true);
        String reason = "Accident";
        String reasonContent = "Accident de la route";
        CancelDataDTO cancelData = new CancelDataDTO(reason, reasonContent, coursier.getId(), delivery.getId());
        when(deliveryServiceMock.replaceOrder(cancelData)).thenReturn(expectedMock);
        mockMvc.perform(put(BASE_URI + delivery.getId() + "/?coursierId=" + coursier.getId())
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cancelData)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8));

        ArgumentCaptor<CancelDataDTO> captor = ArgumentCaptor.forClass(CancelDataDTO.class);
        verify(deliveryServiceMock, times(1)).replaceOrder(captor.capture());
        verifyNoMoreInteractions(deliveryServiceMock);
        assertEquals(captor.getValue(), cancelData);
    }

}
