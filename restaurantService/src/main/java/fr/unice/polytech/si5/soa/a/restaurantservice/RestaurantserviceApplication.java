package fr.unice.polytech.si5.soa.a.restaurantservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication(exclude = {JpaRepositoriesAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class RestaurantserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestaurantserviceApplication.class, args);
	}
}
