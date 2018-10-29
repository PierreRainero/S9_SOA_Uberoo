package fr.unice.polytech.si5.soa.a.configuration;

import fr.unice.polytech.si5.soa.a.communication.bus.*;
import fr.unice.polytech.si5.soa.a.entities.*;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Class name	ApplicationConfiguration
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
// Components to used :
@ComponentScans(value = {
		@ComponentScan("fr.unice.polytech.si5.soa.a.dao"),
		@ComponentScan("fr.unice.polytech.si5.soa.a.services")
})
public class ApplicationConfiguration {
	@Autowired
	private Environment env;

	@Bean
	public DataSource getDataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(env.getProperty("db.driver"));
		dataSource.setUrl(env.getProperty("db.url"));
		dataSource.setUsername(env.getProperty("db.username"));
		dataSource.setPassword(env.getProperty("db.password"));
		return dataSource;
	}

	@Bean
	public LocalSessionFactoryBean getSessionFactory() {
		LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
		factoryBean.setDataSource(getDataSource());

		Properties props = new Properties();
		props.put("hibernate.format_sql", env.getProperty("hibernate.format_sql"));
		props.put("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
		props.put("hibernate.hbm2ddl.auto", env.getProperty("db.hbm2ddl.auto"));
		props.put("hibernate.dialect", env.getProperty("db.dialect"));

		factoryBean.setHibernateProperties(props);
		factoryBean.setAnnotatedClasses(UberooOrder.class, Meal.class, User.class, Restaurant.class, Payment.class, Feedback.class);
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

	//Listener
	@Bean
	@Primary
	public MessageListener messageListener() {
		return new MessageListener();
	}

	public ConsumerFactory<String, PaymentConfirmation> consumerPaymentConfirmationFactory(String groupId) {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, env.getProperty("kafka.bootstrapAddress"));
		props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(PaymentConfirmation.class));
	}


	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, PaymentConfirmation> bankContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, PaymentConfirmation> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerPaymentConfirmationFactory("order"));
		factory.setRecordFilterStrategy(record -> !record.value()
				.getType().equals(PaymentConfirmation.messageType));
		return factory;
	}

	public ConsumerFactory<String, NewMeal> mealFactory(String groupId) {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, env.getProperty("kafka.bootstrapAddress"));
		props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(NewMeal.class));
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, NewMeal> mealContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, NewMeal> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(mealFactory("order"));
		factory.setRecordFilterStrategy(record -> !record.value()
				.getType().equals(NewMeal.messageType));
		return factory;
	}

	public ConsumerFactory<String, NewRestaurant> restaurantFactory(String groupId) {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, env.getProperty("kafka.bootstrapAddress"));
		props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(NewRestaurant.class));
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, NewRestaurant> restaurantContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, NewRestaurant> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(restaurantFactory("order"));
		factory.setRecordFilterStrategy(record -> !record.value()
				.getType().equals(NewRestaurant.messageType));
		return factory;
	}
}