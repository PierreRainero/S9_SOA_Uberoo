package fr.unice.polytech.si5.soa.a.configuration;

import fr.unice.polytech.si5.soa.a.communication.bus.MessageListener;
import fr.unice.polytech.si5.soa.a.communication.bus.MessageProducer;
import fr.unice.polytech.si5.soa.a.communication.bus.NewOrder;
import fr.unice.polytech.si5.soa.a.communication.bus.OrderDelivered;
import fr.unice.polytech.si5.soa.a.entities.Feedback;
import fr.unice.polytech.si5.soa.a.entities.Ingredient;
import fr.unice.polytech.si5.soa.a.entities.Meal;
import fr.unice.polytech.si5.soa.a.entities.Restaurant;
import fr.unice.polytech.si5.soa.a.entities.RestaurantOrder;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

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
		factoryBean.setAnnotatedClasses(RestaurantOrder.class, Meal.class, Ingredient.class, Restaurant.class, Feedback.class);
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
	@Primary
	public MessageListener messageListener() {
		return new MessageListener();
	}

//Producer

//	@Bean
//	public ProducerFactory<String, Message> messageProducerFactory() {
//		Map<String, Object> configProps = new HashMap<>();
//		configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, env.getProperty("kafka.bootstrapAddress"));
//		configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//		configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
//		return new DefaultKafkaProducerFactory<>(configProps);
//	}
//
//	@Bean
//	public KafkaTemplate<String, Message> messageKafkaTemplate() {
//		return new KafkaTemplate<>(messageProducerFactory());
//	}

	//Consumer
	public ConsumerFactory<String, NewOrder> newOrderConsumerFactory(String groupId) {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, env.getProperty("kafka.bootstrapAddress"));
		props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(NewOrder.class));

	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, NewOrder> newOrderConcurrentKafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, NewOrder> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(newOrderConsumerFactory("restaurant"));
		factory.setRecordFilterStrategy(record -> record.value()
					.getType().equals(NewOrder.messageType));
		return factory;
	}

	public ConsumerFactory<String, OrderDelivered> orderDeliveredConsumerFactory(String groupId) {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, env.getProperty("kafka.bootstrapAddress"));
		props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(),
				new JsonDeserializer<>(OrderDelivered.class));
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, OrderDelivered> orderDeliveredConcurrentKafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, OrderDelivered> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(orderDeliveredConsumerFactory("restaurant"));
		factory.setRecordFilterStrategy(record -> record.value()
				.getType().equals(OrderDelivered.messageType));
		return factory;
	}

}
