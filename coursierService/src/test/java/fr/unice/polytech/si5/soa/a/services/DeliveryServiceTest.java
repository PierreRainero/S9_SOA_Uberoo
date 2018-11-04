package fr.unice.polytech.si5.soa.a.services;

import fr.unice.polytech.si5.soa.a.communication.DTO.CancelDataDTO;
import fr.unice.polytech.si5.soa.a.communication.DTO.DeliveryDTO;
import fr.unice.polytech.si5.soa.a.communication.message.Message;
import fr.unice.polytech.si5.soa.a.communication.message.PaymentConfirmation;
import fr.unice.polytech.si5.soa.a.configuration.TestConfiguration;
import fr.unice.polytech.si5.soa.a.dao.ICoursierDao;
import fr.unice.polytech.si5.soa.a.dao.IDeliveryDao;
import fr.unice.polytech.si5.soa.a.entities.Coursier;
import fr.unice.polytech.si5.soa.a.entities.Delivery;
import fr.unice.polytech.si5.soa.a.entities.Restaurant;
import fr.unice.polytech.si5.soa.a.exceptions.CoursierDoesntGetPaidException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknownCoursierException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknownDeliveryException;
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

import java.util.*;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;
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
    @Qualifier("mock")
    @Mock
    private ICoursierDao iCoursierDaoMock;

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

    private Coursier coursier;

    private Restaurant restaurant;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Mockito.reset(iDeliveryDaoMock);
        Mockito.reset(iCoursierDaoMock);

        restaurant = new Restaurant();
        restaurant.setId(0);
        restaurant.setLongitude(0.);
        restaurant.setLatitude(0.);

        coursier = new Coursier();
        coursier.setAccountNumber("FR89 3704 0044 0532 0130 00");

        deliveryTodo = new Delivery();
        deliveryTodo.setDeliveryAddress(ADDRESS);
        deliveryTodo.setCoursier(coursier);
        deliveryTodo.setRestaurant(restaurant);

        coursier.addDelivery(deliveryTodo);

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
        when(iDeliveryDaoMock.addDelivery(any())).thenReturn(deliveryTodo);
        DeliveryDTO returnedDelivery = deliveryService.addDelivery(deliveryTodo.toDTO());
        assertEquals(returnedDelivery, deliveryTodo.toDTO());
    }

    @Test
    public void updateDelivery() throws UnknownCoursierException, UnknownDeliveryException {
        when(iDeliveryDaoMock.updateDelivery(deliveryTodo)).thenReturn(deliveryDone);
        when(iDeliveryDaoMock.findDeliveryById(deliveryTodo.getId())).thenReturn(Optional.of(deliveryTodo));
        coursier.setId(5);
        deliveryTodo.setCoursier(coursier);
        when(iCoursierDaoMock.findCoursierById(coursier.getId())).thenReturn(Optional.of(coursier));
        MessageProducer spy = Mockito.spy(messageProducerMock);
        doNothing().when(spy).sendMessage(any(Message.class));
        deliveryTodo.setCoursier(coursier);
        DeliveryDTO returnedDelivery = deliveryService.updateDelivery(deliveryTodo.toDTO());
        assertNotEquals(returnedDelivery, deliveryTodo.toDTO());
        assertEquals(returnedDelivery, deliveryDone.toDTO());
    }

    @Test
    public void receiveNewPayment() throws UnknownDeliveryException, CoursierDoesntGetPaidException {
        assertFalse(this.deliveryDone.getCoursierGetPaid());
        when(iDeliveryDaoMock.findDeliveryById(this.deliveryDone.getId())).thenReturn(Optional.of(this.deliveryDone));
        when(iDeliveryDaoMock.updateDelivery(this.deliveryDone)).thenReturn(this.deliveryDone);
        PaymentConfirmation message = new PaymentConfirmation();
        message.setId(this.deliveryDone.getId());
        message.setStatus(true);
        Delivery deliveryAfterPayment = this.deliveryService.receiveNewPayment(message);
        this.deliveryDone.setCoursierGetPaid(true);
        assertEquals(deliveryAfterPayment, this.deliveryDone);
    }

    @Test
    public void assignDelivery() throws UnknownDeliveryException, UnknownCoursierException {
        assertNull(deliveryTodo.getCoursier().getId());
        assertNull(deliveryTodo.getCreationDate());
        when(iDeliveryDaoMock.findDeliveryById(this.deliveryTodo.getId())).thenReturn(Optional.ofNullable(this.deliveryTodo));
        when(iCoursierDaoMock.findCoursierById(this.coursier.getId())).thenReturn(Optional.ofNullable(this.coursier));
        this.deliveryTodo.setCoursier(coursier);
        this.deliveryTodo.setCreationDate(new Date());
        this.coursier.addDelivery(this.deliveryTodo);
        when(iCoursierDaoMock.updateCoursier(this.coursier)).thenReturn(this.coursier);
        when(iDeliveryDaoMock.updateDelivery(this.deliveryTodo)).thenReturn(this.deliveryTodo);
        DeliveryDTO deliveryDTO = this.deliveryService.assignDelivery(this.deliveryTodo.getId(), coursier.getId());
        assertEquals(this.deliveryTodo.toDTO(), deliveryDTO);
    }

    @Test
    public void replaceOrderTest() throws UnknownDeliveryException, UnknownCoursierException {
        when(iCoursierDaoMock.findCoursierById(coursier.getId())).thenReturn(Optional.of(coursier));
        when(iCoursierDaoMock.updateCoursier(coursier)).thenReturn(coursier);
        when(iDeliveryDaoMock.findDeliveryById(deliveryTodo.getId())).thenReturn(Optional.of(deliveryTodo));
        when(iDeliveryDaoMock.updateDelivery(deliveryTodo)).thenReturn(deliveryTodo);
        String reasonType = "Accident";
        String reasonContent = "Accident de la route sur la rue Evariste Galois";
        CancelDataDTO cancelData = new CancelDataDTO(reasonType, reasonContent, coursier.getId(), deliveryTodo.getId());
        assertFalse(deliveryTodo.cancel);
        DeliveryDTO deliveryAfterReplace = this.deliveryService.replaceOrder(cancelData);
        assertTrue(deliveryAfterReplace.cancel);
    }

}
