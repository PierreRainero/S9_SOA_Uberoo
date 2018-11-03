package fr.unice.polytech.si5.soa.a.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
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

import fr.unice.polytech.si5.soa.a.communication.UserDTO;
import fr.unice.polytech.si5.soa.a.configuration.TestConfiguration;
import fr.unice.polytech.si5.soa.a.configuration.WebApplicationConfiguration;
import fr.unice.polytech.si5.soa.a.entities.User;
import fr.unice.polytech.si5.soa.a.services.IUserService;
import fr.unice.polytech.si5.soa.a.util.TestUtil;

/**
 * Class name	UserControllerTest
 * Date			03/11/2018
 * @author		PierreRainero
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfiguration.class, WebApplicationConfiguration.class})
@WebAppConfiguration
public class UserControllerTest {
	private final static String BASE_URI = "/users";
	
	private MockMvc mockMvc;
	
	@Qualifier("mock")
	@Autowired
	@Mock
    private IUserService userServiceMock;
    
    @Autowired
    @InjectMocks
    private UserController userController;
    
	private UserDTO bob;
	
	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		Mockito.reset(userServiceMock);
		mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
		
		User user = new User();
		user.setFirstName("Bob");
		user.setLastName("Harington");
		
		bob = user.toDTO();
	}
	
	@Test
    public void addUserUsingHTTPPost() throws Exception {
		when(userServiceMock.addUser(any(UserDTO.class))).thenReturn(bob);

		mockMvc.perform(post(BASE_URI)
               .contentType(TestUtil.APPLICATION_JSON_UTF8)
               .content(TestUtil.convertObjectToJsonBytes(bob))
        ).andExpect(status().isOk())
         .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8));
        
        ArgumentCaptor<UserDTO> captor = ArgumentCaptor.forClass(UserDTO.class);
        verify(userServiceMock, times(1)).addUser(captor.capture());
        verifyNoMoreInteractions(userServiceMock);
        
        UserDTO userSent = captor.getValue();
        assertNotNull(userSent);
        assertEquals(bob.getFirstName(), userSent.getFirstName());
        assertEquals(bob.getLastName(), userSent.getLastName());
	}
}
