package fr.unice.polytech.si5.soa.a.controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import fr.unice.polytech.si5.soa.a.communication.PaymentDTO;
import fr.unice.polytech.si5.soa.a.configuration.TestConfiguration;
import fr.unice.polytech.si5.soa.a.configuration.WebApplicationConfiguration;
import fr.unice.polytech.si5.soa.a.entities.Payment;
import fr.unice.polytech.si5.soa.a.entities.Restaurant;
import fr.unice.polytech.si5.soa.a.entities.UberooOrder;
import fr.unice.polytech.si5.soa.a.entities.User;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowOrderException;
import fr.unice.polytech.si5.soa.a.services.IPaymentService;
import fr.unice.polytech.si5.soa.a.util.TestUtil;

/**
 * Class name	PaymentControllerTest
 * Date			22/10/2018
 * @author		PierreRainero
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfiguration.class, WebApplicationConfiguration.class})
@WebAppConfiguration
public class PaymentControllerTest {
	private final static String BASE_URI = "/payments";
	
	private final static String ERROR_UNKNOW_ORDER = "Can't find order with id = -1";
	
	private MockMvc mockMvc;
	
	@Qualifier("mock")
	@Autowired
	@Mock
	private IPaymentService paymentService;
	
	@Autowired
    @InjectMocks
    private PaymentController paymentController;
	
	private Payment payment;
	private UberooOrder order;
	
	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		Mockito.reset(paymentService);
		
		mockMvc = MockMvcBuilders.standaloneSetup(paymentController).build();
		
		order = new UberooOrder();
		order.setRestaurant(new Restaurant());
		order.setDeliveryAddress("930 Route des Colles, 06410 Biot");
		order.setTransmitter(new User());

		payment = new Payment();
		payment.setAccount("FR89 3704 0044 0532 0130 00");
		payment.setAmount(45.25);
		payment.setOrder(order);
	}
	
	@Test
	public void payAnOrderUsingHTTPPost() throws Exception {
		when(paymentService.addPayment(any(PaymentDTO.class), anyInt())).thenReturn(payment.toDTO());

		mockMvc.perform(post("/orders/1"+BASE_URI)
               .contentType(TestUtil.APPLICATION_JSON_UTF8)
               .content(TestUtil.convertObjectToJsonBytes(payment))
        ).andExpect(status().isOk())
         .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8));
        
		ArgumentCaptor<PaymentDTO> paymentCaptorPaymentParam = ArgumentCaptor.forClass(PaymentDTO.class);
        ArgumentCaptor<Integer> paymentCaptorOrderParam = ArgumentCaptor.forClass(Integer.class);
        verify(paymentService, times(1)).addPayment(paymentCaptorPaymentParam.capture(), paymentCaptorOrderParam.capture());
        verifyNoMoreInteractions(paymentService);
        
        PaymentDTO paymentValue = paymentCaptorPaymentParam.getValue();
        assertEquals(payment.getAccount(), paymentValue.getAccount());
        
        Integer orderValue = paymentCaptorOrderParam.getValue();
        assertEquals(new Integer(1), orderValue);
	}
	
	@Test
	public void payANonExistingOrderInServiceLayer() throws Exception {
		when(paymentService.addPayment(any(PaymentDTO.class), anyInt())).thenThrow(new UnknowOrderException(ERROR_UNKNOW_ORDER));

		mockMvc.perform(post("/orders/1"+BASE_URI)
               .contentType(TestUtil.APPLICATION_JSON_UTF8)
               .content(TestUtil.convertObjectToJsonBytes(payment))
        ).andExpect(status().isNotFound())
		 .andExpect(content().string(ERROR_UNKNOW_ORDER));
        
        ArgumentCaptor<PaymentDTO> paymentCaptorPaymentParam = ArgumentCaptor.forClass(PaymentDTO.class);
        ArgumentCaptor<Integer> paymentCaptorOrderParam = ArgumentCaptor.forClass(Integer.class);
        verify(paymentService, times(1)).addPayment(paymentCaptorPaymentParam.capture(), paymentCaptorOrderParam.capture());
        verifyNoMoreInteractions(paymentService);
        
        PaymentDTO paymentValue = paymentCaptorPaymentParam.getValue();
        assertEquals(payment.getAccount(), paymentValue.getAccount());
        
        Integer orderValue = paymentCaptorOrderParam.getValue();
        assertEquals(new Integer(1), orderValue);
	}
}
