package fr.unice.polytech.si5.soa.a.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.SQLGrammarException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import fr.unice.polytech.si5.soa.a.configuration.TestConfiguration;
import fr.unice.polytech.si5.soa.a.entities.Delivery;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfiguration.class})
public class DeliveryDaoTest {
    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private IDeliveryDao deliveryDao;

    private Delivery deliveryToDo;
    private Delivery deliveryDone;

    private List<Delivery> deliveries;

    @BeforeEach
    public void setUp(){
        deliveries = new ArrayList<>();
        deliveryToDo = new Delivery();
        deliveryToDo.setDeliveryAddress("140 sentier des hautes breguières");
        
        deliveryDone = new Delivery();
        deliveryDone.setDeliveryAddress("5 rue de l'hôpital");
        deliveryDone.state = true;

        deliveries.add(deliveryToDo);
        deliveries.add(deliveryDone);

        Session session = sessionFactory.openSession();
        try{
            session.save(deliveryToDo);
            session.save(deliveryDone);
            session.beginTransaction().commit();
        }catch (SQLGrammarException e){
            session.getTransaction().rollback();
        }finally {
            session.close();
        }
    }

    @AfterEach
    public void cleanUp(){
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try{
            transaction = session.beginTransaction();
            
            deliveries.forEach(session::delete);
            session.flush();
            transaction.commit();
            
            deliveryToDo = null;
            deliveryDone = null;
        }catch (SQLGrammarException e){
            session.getTransaction().rollback();
        }finally {
            session.close();
        }
    }

    @Test
    public void getDeliveriesToDo(){
        List<Delivery> deliveryList = deliveryDao.getDeliveriesToDo();
        assertTrue(deliveryList.size() == 1);
        assertEquals(deliveryList.get(0),deliveryToDo);
    }

    @Test
    public void updateDelivery(){
        deliveryToDo.state = true;
        deliveryDao.updateDelivery(deliveryToDo);
        assertTrue(deliveryDao.getDeliveriesToDo().stream().allMatch(delivery -> delivery.state));
    }

    @Test
    public void addDelivery(){
        assertTrue(deliveryDao.getDeliveriesToDo().size() == 1);
        Delivery newDelivery = new Delivery();
        newDelivery.setDeliveryAddress("22 rue des tests unitaires");
        
        deliveryDao.addDelivery(newDelivery);
        List<Delivery> deliveriesToDo = deliveryDao.getDeliveriesToDo();
        assertTrue(deliveriesToDo.size() == 2);
        assertEquals(deliveriesToDo.get(1),newDelivery);
        deliveries.add(newDelivery);
    }
}
