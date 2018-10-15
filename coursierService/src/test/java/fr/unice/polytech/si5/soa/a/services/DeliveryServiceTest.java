package fr.unice.polytech.si5.soa.a.services;

import fr.unice.polytech.si5.soa.a.communication.DeliveryDTO;
import fr.unice.polytech.si5.soa.a.configuration.TestConfiguration;
import fr.unice.polytech.si5.soa.a.dao.IDeliveryDao;
import fr.unice.polytech.si5.soa.a.entities.Delivery;
import fr.unice.polytech.si5.soa.a.services.component.DeliveryServiceImpl;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { TestConfiguration.class })
public class DeliveryServiceTest {
    @Autowired
    @Qualifier("mock")
    @Mock
    private IDeliveryDao iDeliveryDaoMock;

    @Autowired
    @Qualifier("mock")
    @Mock
    private RestTemplate restTemplate;

    @Autowired
    @InjectMocks
    private DeliveryServiceImpl deliveryService;

    private Delivery deliveryTodo;
    private Delivery deliveryDone;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        Mockito.reset(iDeliveryDaoMock);
        Mockito.reset(restTemplate);
        deliveryTodo = new Delivery();
        deliveryDone = new Delivery();
        deliveryDone.state = true;
    }

    @AfterEach
    public void cleanUp(){
        deliveryTodo = null;
    }

    @Test
    public void getDeliveriesToDo(){
        List<Delivery> deliveries = Collections.singletonList(deliveryTodo);
        when(iDeliveryDaoMock.getDeliveriesToDo()).thenReturn(deliveries);
        List<DeliveryDTO> deliveriesReturned = deliveryService.getDeliveriesToDo();
        assertTrue(deliveriesReturned.size() == 1);
        assertEquals(deliveriesReturned.get(0),deliveryTodo.toDTO());
    }

    @Test
    public void addDelivery(){


    }


}
