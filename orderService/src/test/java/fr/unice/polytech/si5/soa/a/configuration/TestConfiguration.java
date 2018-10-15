package fr.unice.polytech.si5.soa.a.configuration;

import fr.unice.polytech.si5.soa.a.dao.ICatalogDao;
import fr.unice.polytech.si5.soa.a.dao.IOrderTakerDao;
import fr.unice.polytech.si5.soa.a.dao.IUserDao;
import fr.unice.polytech.si5.soa.a.entities.Meal;
import fr.unice.polytech.si5.soa.a.entities.UberooOrder;
import fr.unice.polytech.si5.soa.a.entities.User;
import fr.unice.polytech.si5.soa.a.message.MessageProducer;
import fr.unice.polytech.si5.soa.a.services.ICatalogService;
import fr.unice.polytech.si5.soa.a.services.IOrderTakerService;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Class name	TestConfiguration
 * Date			29/09/2018
 *
 * @author PierreRainero
 */
@Configuration
@PropertySources({
		@PropertySource("classpath:db.properties"),
		@PropertySource("classpath:application.properties")
})
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
		factoryBean.setAnnotatedClasses(UberooOrder.class, Meal.class, User.class);
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
	public ProducerFactory<String, String> producerFactory() {
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, env.getProperty("kafka.bootstrapAddress"));
		configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		return new DefaultKafkaProducerFactory<>(configProps);
	}

	@Bean
	public KafkaTemplate<String, String> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}

	@Qualifier("mock")
	@Bean
	public IOrderTakerDao iOrderTakerDao() {
		return Mockito.mock(IOrderTakerDao.class);
	}

	@Qualifier("mock")
	@Bean
	public IUserDao iUserDao() {
		return Mockito.mock(IUserDao.class);
	}

	@Qualifier("mock")
	@Bean
	public ICatalogDao iCatalogDao() {
		return Mockito.mock(ICatalogDao.class);
	}

	@Qualifier("mock")
	@Bean
	public IOrderTakerService iOrderTakerService() {
		return Mockito.mock(IOrderTakerService.class);
	}

	@Qualifier("mock")
	@Bean
	public ICatalogService iCatalogService() {
		return Mockito.mock(ICatalogService.class);
	}

	@Qualifier("mock")
	@Bean
	public RestTemplate restTemplate() {
		return Mockito.mock(RestTemplate.class);
	}
}