package fr.unice.polytech.si5.soa.a.restaurantservice.configuration;

import org.springframework.context.annotation.Configuration;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;
import fr.unice.polytech.si5.soa.a.restaurantservice.dao.IOrderDao;
import fr.unice.polytech.si5.soa.a.restaurantservice.model.OrderToPrepare;


@Configuration
@PropertySource("classpath:db.properties")
@EnableTransactionManagement
// Components to used
@ComponentScans(value = { 
		@ComponentScan("fr.unice.polytech.si5.soa.a.restaurantservice.dao"),
		@ComponentScan("fr.unice.polytech.si5.soa.a.restaurantservice.services")
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
		factoryBean.setAnnotatedClasses(OrderToPrepare.class);
		return factoryBean;
	}

	@Bean
	public HibernateTransactionManager getTransactionManager() {
		HibernateTransactionManager transactionManager = new HibernateTransactionManager();
		transactionManager.setSessionFactory(getSessionFactory().getObject());
		return transactionManager;
	}
	
	@Qualifier("mock")
	@Bean
    public IOrderDao iOrderDao() {
        return Mockito.mock(IOrderDao.class);
    }
	
	@Qualifier("mock")
	@Bean
	public RestTemplate restTemplate() {
	    return Mockito.mock(RestTemplate.class);
	}
}