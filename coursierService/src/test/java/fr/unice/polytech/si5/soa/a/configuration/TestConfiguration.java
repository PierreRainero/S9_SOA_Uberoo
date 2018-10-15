package fr.unice.polytech.si5.soa.a.configuration;

import fr.unice.polytech.si5.soa.a.dao.IDeliveryDao;
import fr.unice.polytech.si5.soa.a.entities.Delivery;
import fr.unice.polytech.si5.soa.a.services.IDeliveryService;
import org.apache.commons.dbcp2.BasicDataSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.util.Properties;

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

        factoryBean.setHibernateProperties(props);
        factoryBean.setAnnotatedClasses(Delivery.class);
        return factoryBean;
    }

    @Bean
    public HibernateTransactionManager getTransactionManager() {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(getSessionFactory().getObject());
        return transactionManager;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Qualifier("mock")
    @Bean
    public IDeliveryDao iDeliveryDao() {
        return Mockito.mock(IDeliveryDao.class);
    }
}
