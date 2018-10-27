package fr.unice.polytech.si5.soa.a.services;

import fr.unice.polytech.si5.soa.a.communication.DeliveryDTO;
import fr.unice.polytech.si5.soa.a.communication.Message;
import fr.unice.polytech.si5.soa.a.configuration.TestConfiguration;
import fr.unice.polytech.si5.soa.a.dao.IDeliveryDao;
import fr.unice.polytech.si5.soa.a.entities.Delivery;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowDeliveryException;
import fr.unice.polytech.si5.soa.a.message.MessageProducer;
import fr.unice.polytech.si5.soa.a.services.component.DeliveryServiceImpl;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfiguration.class})
public class DeliveryServiceTest {
    @Autowired
    @Qualifier("mock")
    @Mock
    private IDeliveryDao iDeliveryDaoMock;

    @Autowired
    @InjectMocks
    private DeliveryServiceImpl deliveryService;

    @Autowired
    @Mock
    private MessageProducer messageProducerMock;

    private Delivery deliveryTodo;
    private Delivery deliveryDone;

    private static final String ADDRESS = "Evariste Galois";
    private Delivery deliveryBelow10;
    private Delivery deliveryOver10;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Mockito.reset(iDeliveryDaoMock);

        deliveryTodo = new Delivery();
        deliveryTodo.setDeliveryAddress(ADDRESS);
        deliveryDone = new Delivery();
        deliveryDone.setState(true);
        deliveryDone.setDeliveryAddress(ADDRESS);

        deliveryBelow10 = new Delivery();
        deliveryBelow10.setDeliveryAddress(ADDRESS);
        deliveryBelow10.setState(false);
        deliveryBelow10.setLatitude(0.0);
        deliveryBelow10.setLongitude(0.0);

        deliveryOver10 = new Delivery();
        deliveryOver10.setDeliveryAddress(ADDRESS);
        deliveryOver10.setState(false);
        deliveryOver10.setLatitude(1.0);
        deliveryOver10.setLongitude(1.0);
    }

    @AfterEach
    public void cleanUp() {
        deliveryTodo = null;
        deliveryDone = null;
        deliveryBelow10 = null;
        deliveryOver10 = null;
    }

    @Test
    public void getDeliveriesToDo() {
        List<Delivery> deliveries = Collections.singletonList(deliveryTodo);
        when(iDeliveryDaoMock.getDeliveriesToDo()).thenReturn(deliveries);
        List<DeliveryDTO> deliveriesReturned = deliveryService.getDeliveriesToDo();
        assertTrue(deliveriesReturned.size() == 1);
        assertEquals(deliveriesReturned.get(0), deliveryTodo.toDTO());
    }

    @Test
    public void getDeliveriesToDoWithPositionTest() {
        List<Delivery> deliveries = Arrays.asList(deliveryBelow10, deliveryOver10);
        when(iDeliveryDaoMock.getDeliveriesToDo()).thenReturn(deliveries);
        double latitudeCoursier = 0.05, longitudeCoursier = 0.05;
        List<DeliveryDTO> deliveriesReturned = deliveryService.getDeliveriesToDo(latitudeCoursier, longitudeCoursier);
        assertTrue(deliveriesReturned.size() == 1);
        assertEquals(deliveriesReturned.get(0), deliveryBelow10.toDTO());
    }

    @Test
    public void addDelivery() {
        when(iDeliveryDaoMock.addDelivery(deliveryTodo)).thenReturn(deliveryTodo);
        DeliveryDTO returnedDelivery = deliveryService.addDelivery(deliveryTodo.toDTO());
        assertEquals(returnedDelivery, deliveryTodo.toDTO());
    }

    @Test
    public void updateDelivery() throws UnknowDeliveryException {
        when(iDeliveryDaoMock.updateDelivery(deliveryTodo)).thenReturn(deliveryDone);
        when(iDeliveryDaoMock.findDeliveryById(deliveryTodo.getId())).thenReturn(Optional.of(deliveryTodo));
        MessageProducer spy = Mockito.spy(messageProducerMock);
        doNothing().when(spy).sendMessage(any(Message.class));
        DeliveryDTO returnedDelivery = deliveryService.updateDelivery(deliveryTodo.toDTO());
        assertNotEquals(returnedDelivery, deliveryTodo.toDTO());
        assertEquals(returnedDelivery, deliveryDone.toDTO());
    }


}
