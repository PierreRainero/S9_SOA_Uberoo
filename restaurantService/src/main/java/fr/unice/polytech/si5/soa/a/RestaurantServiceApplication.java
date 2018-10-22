package fr.unice.polytech.si5.soa.a;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

import fr.unice.polytech.si5.soa.a.communication.bus.MessageListener;

import java.util.concurrent.TimeUnit;

@SpringBootApplication(exclude = {JpaRepositoriesAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class RestaurantServiceApplication {
	public static void main(String[] args) throws Exception{
		ConfigurableApplicationContext context = SpringApplication.run(RestaurantServiceApplication.class, args);
		MessageListener listener = context.getBean(MessageListener.class);
		listener.getLatch().await(10, TimeUnit.SECONDS);
	}
}
