package fr.unice.polytech.si5.soa.a.restaurantservice.configuration;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import fr.unice.polytech.si5.soa.a.restaurantservice.model.OrderToPrepare;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@PropertySource("classpath:application.properties")
@EnableTransactionManagement
// Components to used :
@ComponentScans(value = { 
		@ComponentScan(" fr.unice.polytech.si5.soa.a.restaurantservice.dao"),
		@ComponentScan("fr.unice.polytech.si5.soa.a.restaurantservice.services")
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
		factoryBean.setAnnotatedClasses(OrderToPrepare.class);
		return factoryBean;
	}

	@Bean
	public HibernateTransactionManager getTransactionManager() {
		HibernateTransactionManager transactionManager = new HibernateTransactionManager();
		transactionManager.setSessionFactory(getSessionFactory().getObject());
		return transactionManager;
	}
}