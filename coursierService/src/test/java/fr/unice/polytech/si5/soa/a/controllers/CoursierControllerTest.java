package fr.unice.polytech.si5.soa.a.controllers;


import fr.unice.polytech.si5.soa.a.communication.CoursierStatistics;
import fr.unice.polytech.si5.soa.a.configuration.TestConfiguration;
import fr.unice.polytech.si5.soa.a.configuration.WebApplicationConfiguration;
import fr.unice.polytech.si5.soa.a.entities.Coursier;
import fr.unice.polytech.si5.soa.a.exceptions.UnknownRestaurantException;
import fr.unice.polytech.si5.soa.a.services.ICoursierService;
import fr.unice.polytech.si5.soa.a.util.TestUtil;
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
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        TestConfiguration.class,
        WebApplicationConfiguration.class
})
@WebAppConfiguration
public class CoursierControllerTest {
    private final static String BASE_URI = "/coursiers/";
    private MockMvc mockMvc;

    @Qualifier("mock")
    @Autowired
    @Mock
    private ICoursierService coursierServiceMock;

    @Autowired
    @InjectMocks
    private CoursierController coursierController;

    private Coursier coursier;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Mockito.reset(coursierServiceMock);

        mockMvc = MockMvcBuilders.standaloneSetup(coursierController).build();

        coursier = new Coursier();
        coursier.setName("Jean");
        coursier.setAccountNumber("FR89 3704 0044 0532 0130 00");
        coursier.setId(10);
    }

    @Test
    public void getCoursier() throws Exception {
        when(coursierServiceMock.getCoursier(coursier.getId())).thenReturn(coursier.toDto());
        mockMvc.perform(get(BASE_URI + coursier.getId())
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(coursier.toDto())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8));
    }

    @Test
    public void getCoursierStatistics() throws Exception, UnknownRestaurantException {
        CoursierStatistics coursierStatistics = new CoursierStatistics();
        coursierStatistics.setSpeed(12.);
        when(coursierServiceMock.getCoursierStatistics(coursier.getId(), 1)).thenReturn(coursierStatistics);
        mockMvc.perform(get(BASE_URI + coursier.getId() + "/deliveries/" + "?idRestaurant=1")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(coursierStatistics)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8));
    }
}
