package fr.unice.polytech.si5.soa.a.services;

import fr.unice.polytech.si5.soa.a.communication.CoursierStatistics;
import fr.unice.polytech.si5.soa.a.dao.ICoursierDao;
import fr.unice.polytech.si5.soa.a.dao.IRestaurantDao;
import fr.unice.polytech.si5.soa.a.entities.Coursier;
import fr.unice.polytech.si5.soa.a.entities.Delivery;
import fr.unice.polytech.si5.soa.a.entities.Restaurant;
import fr.unice.polytech.si5.soa.a.exceptions.UnknownCoursierException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknownRestaurantException;
import fr.unice.polytech.si5.soa.a.message.MessageProducer;
import fr.unice.polytech.si5.soa.a.services.component.CoursierServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class CoursierServiceTest {

    @Autowired
    @Qualifier("mock")
    @Mock
    private IRestaurantDao iRestaurantDaoMock;

    @Autowired
    @Qualifier("mock")
    @Mock
    private ICoursierDao iCoursierDaoMock;

    @Autowired
    @InjectMocks
    private CoursierServiceImpl coursierServiceImpl;

    @Autowired
    @Mock
    private MessageProducer messageProducerMock;

    private Coursier coursier;

    private Restaurant restaurant;

    private Delivery delivery;
    private Delivery delivery2;


    @BeforeEach
    public void setUp() throws ParseException {
        MockitoAnnotations.initMocks(this);
        Mockito.reset(iRestaurantDaoMock);
        Mockito.reset(iCoursierDaoMock);

        coursier = new Coursier();
        coursier.setId(19);
        coursier.setAccountNumber("FR89 3704 0044 0532 0130 00");
        coursier.setName("Jean");

        restaurant = new Restaurant();
        restaurant.setId(20);
        restaurant.setLongitude(0.5);
        restaurant.setLatitude(0.5);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

        this.delivery = new Delivery();
        delivery.setCoursier(coursier);
        delivery.setRestaurant(restaurant);
        delivery.setLongitude(0.);
        delivery.setLatitude(0.);
        delivery.setCreationDate(dateFormat.parse("00/00/0000 00:00:00"));
        delivery.setDeliveryDate(dateFormat.parse("00/00/0000 01:00:00"));

        this.delivery2 = new Delivery();
        delivery2.setCoursier(coursier);
        delivery2.setRestaurant(restaurant);
        delivery2.setLatitude(1.);
        delivery2.setLongitude(1.);
        delivery2.setCreationDate(dateFormat.parse("00/00/0000 01:00:00"));
        delivery2.setDeliveryDate(dateFormat.parse("00/00/0000 02:00:00"));

        coursier.addDelivery(delivery);
        coursier.addDelivery(delivery2);

        when(iCoursierDaoMock.findCoursierById(coursier.getId())).thenReturn(Optional.of(coursier));
    }

    @AfterEach
    public void cleanUp() {
        coursier = null;
        restaurant = null;
        delivery2 = null;
        delivery = null;
    }

    @Test
    public void getCoursier() throws UnknownCoursierException {
        assertEquals(coursier.toDto(), coursierServiceImpl.getCoursier(coursier.getId()));
    }

    @Test
    public void getCoursierStatistics() throws UnknownCoursierException, UnknownRestaurantException {
        when(iRestaurantDaoMock.findRestaurantById(restaurant.getId())).thenReturn(Optional.of(restaurant));
        CoursierStatistics coursierStatistics = coursierServiceImpl.getCoursierStatistics(coursier.getId(), restaurant.getId());
        assertEquals(coursierStatistics.getSpeed().intValue(), 78);
    }

    /*
    @Test
    public void getDeliveriesToDo() {
        List<Delivery> deliveries = Collections.singletonList(deliveryTodo);
        when(iDeliveryDaoMock.getDeliveriesToDo()).thenReturn(deliveries);
        List<DeliveryDTO> deliveriesReturned = deliveryService.getDeliveriesToDo();
        assertTrue(deliveriesReturned.size() == 1);
        assertEquals(deliveriesReturned.get(0), deliveryTodo.toDTO());
    }
    */

}
