package fr.unice.polytech.si5.soa.a.configuration;

import fr.unice.polytech.si5.soa.a.communication.bus.Message;
import fr.unice.polytech.si5.soa.a.communication.bus.MessageProducer;
import fr.unice.polytech.si5.soa.a.dao.IMealDao;
import fr.unice.polytech.si5.soa.a.dao.IOrderDao;
import fr.unice.polytech.si5.soa.a.dao.IRestaurantDao;
import fr.unice.polytech.si5.soa.a.entities.Ingredient;
import fr.unice.polytech.si5.soa.a.entities.Meal;
import fr.unice.polytech.si5.soa.a.entities.Restaurant;
import fr.unice.polytech.si5.soa.a.entities.RestaurantOrder;
import fr.unice.polytech.si5.soa.a.services.IMealService;
import fr.unice.polytech.si5.soa.a.services.IOrderService;
import fr.unice.polytech.si5.soa.a.services.IRestaurantService;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Class name	TestConfiguration
 * Date			29/09/2018
 * @author		PierreRainero
 */
@Configuration
@PropertySource("classpath:db.properties")
@EnableTransactionManagement
// Components to used
@ComponentScans(value = { 
		@ComponentScan("fr.unice.polytech.si5.soa.a.dao"),
		@ComponentScan("fr.unice.polytech.si5.soa.a.services")
})
public class TestConfiguration {
	@Autowired
	private Environment env;

	@Bean
	public DataSource getDataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(env.getProperty("hsqldb.driver"));
		dataSource.setUrl(env.getProperty("hsqldb.url"));
		dataSource.setUsername(env.getProperty("hsqldb.username"));
		dataSource.setPassword(env.getProperty("hsqldb.password"));
		return dataSource;
	}

	@Bean
	public LocalSessionFactoryBean getSessionFactory() {
		LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
		factoryBean.setDataSource(getDataSource());

		Properties props = new Properties();
		props.put("hibernate.format_sql", env.getProperty("hibernate.format_sql"));
		props.put("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
		props.put("hibernate.hbm2ddl.auto", env.getProperty("hsqldb.hbm2ddl.auto"));
		props.put("hibernate.dialect", env.getProperty("hsqldb.dialect"));

		// Entities
		factoryBean.setHibernateProperties(props);
		factoryBean.setAnnotatedClasses(RestaurantOrder.class, Meal.class, Ingredient.class, Restaurant.class);
		return factoryBean;
	}

	@Bean
	public HibernateTransactionManager getTransactionManager() {
		HibernateTransactionManager transactionManager = new HibernateTransactionManager();
		transactionManager.setSessionFactory(getSessionFactory().getObject());
		return transactionManager;
	}
	
	@Bean
	@Primary
	public MessageProducer messageProducer() {
		return new MessageProducer();
	}

	@Bean
    public ProducerFactory<String, Message> messageProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, env.getProperty("kafka.bootstrapAddress"));
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }
    
    @Bean
    public KafkaTemplate<String, Message> messageKafkaTemplate() {
        return new KafkaTemplate<>(messageProducerFactory());
    }
    
    @Qualifier("mock")
	@Bean
    public IRestaurantDao iRestaurantDao() {
        return Mockito.mock(IRestaurantDao.class);
    }
    
    @Qualifier("mock")
	@Bean
    public IMealDao iMealDao() {
        return Mockito.mock(IMealDao.class);
    }
    
    @Qualifier("mock")
	@Bean
    public IOrderDao iOrderDao() {
        return Mockito.mock(IOrderDao.class);
    }
	
	@Qualifier("mock")
	@Bean
    public IOrderService iOrderService() {
        return Mockito.mock(IOrderService.class);
    }
	
	@Qualifier("mock")
	@Bean
    public IRestaurantService iRestaurantService() {
        return Mockito.mock(IRestaurantService.class);
    }
	
	@Qualifier("mock")
	@Bean
    public IMealService iMealService() {
        return Mockito.mock(IMealService.class);
    }
}