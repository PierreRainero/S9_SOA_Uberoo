package fr.unice.polytech.si5.soa.a.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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

import fr.unice.polytech.si5.soa.a.communication.UserDTO;
import fr.unice.polytech.si5.soa.a.configuration.TestConfiguration;
import fr.unice.polytech.si5.soa.a.dao.IUserDao;
import fr.unice.polytech.si5.soa.a.entities.User;
import fr.unice.polytech.si5.soa.a.services.component.UserServiceImpl;

/**
 * Class name	UserServiceTest
 * Date			03/11/2018
 *
 * @author 		PierreRainero
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfiguration.class})
public class UserServiceTest {
	@Autowired
	@Qualifier("mock")
	@Mock
	private IUserDao userDaoMock;
	
	@Autowired
	@InjectMocks
	private UserServiceImpl userService;
	
	private User bob;
	
	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		Mockito.reset(userDaoMock);

		bob = new User();
		bob.setFirstName("Bob");
		bob.setLastName("Harington");
	}
	
	
	@Test
	public void addANewUser() throws Exception {
		when(userDaoMock.addUser(any(User.class))).thenReturn(bob);

		UserDTO userReceived = userService.addUser(bob.toDTO());
		assertNotNull(userReceived);
		assertEquals(bob.getFirstName(), userReceived.getFirstName());
		assertEquals(bob.getLastName(), userReceived.getLastName());
	}
}
