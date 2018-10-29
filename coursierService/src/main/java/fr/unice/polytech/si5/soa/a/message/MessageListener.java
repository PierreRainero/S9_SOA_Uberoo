package fr.unice.polytech.si5.soa.a.message;

import fr.unice.polytech.si5.soa.a.communication.PaymentConfirmation;
import fr.unice.polytech.si5.soa.a.exceptions.CoursierDoesntGetPaidException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknownDeliveryException;
import fr.unice.polytech.si5.soa.a.services.IDeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.concurrent.CountDownLatch;

/**
 * Class MessageListener
 *
 * @author Joël CANCELA VAZ
 */
public class MessageListener {

    @Autowired
    private IDeliveryService deliveryService;

    private CountDownLatch latch = new CountDownLatch(3);

    @KafkaListener(topics = "${coursier.topic.name}", containerFactory = "paymentContainerFactory")
    public void listenNewPayment(PaymentConfirmation message) throws UnknownDeliveryException, CoursierDoesntGetPaidException {
        System.out.println("Received new payment for coursier: " + message.getId());
        deliveryService.receiveNewPayment(message);
        latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }
}
