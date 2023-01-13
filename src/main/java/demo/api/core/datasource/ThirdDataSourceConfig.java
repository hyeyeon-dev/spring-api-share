//package demo.api.core.datasource;
//
//import org.hibernate.cfg.Environment;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
//import org.springframework.transaction.PlatformTransactionManager;
//
//import javax.persistence.EntityManagerFactory;
//import javax.sql.DataSource;
//import java.util.Properties;
//
//@Configuration
//@EnableJpaRepositories(
//        basePackages = {"demo.api.jpa.erp.repository"},
//        entityManagerFactoryRef = "tEntityManagerFactory",
//        transactionManagerRef = "tTransactionManager"
//)
//public class ThirdDataSourceConfig {
//    public static final String PREFIX = "t";
//
//    @Bean(name=PREFIX+"DataSource")
//    @ConfigurationProperties(prefix = "spring.datasource-third")
//    public DataSource dataSource() {
//        return DataSourceBuilder.create().build();
//    }
//
//    @Bean(name=PREFIX+"EntityManagerFactory")
//    public EntityManagerFactory entityManagerFactory(@Qualifier(PREFIX+"DataSource") DataSource dataSource) {
//        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
//        em.setDataSource(dataSource);
//        em.setPackagesToScan("demo.api.jpa.erp.entity");
//        em.setPersistenceUnitName(PREFIX+"EntityManager");
//
//        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
//        em.setJpaVendorAdapter(vendorAdapter);
//
//        Properties jpaProperties = new Properties();
//        jpaProperties.put(Environment.SHOW_SQL, false);
//        jpaProperties.put(Environment.HBM2DDL_AUTO, "none");
//        jpaProperties.put(Environment.FORMAT_SQL, false);
//        jpaProperties.put(Environment.DEFAULT_BATCH_FETCH_SIZE, 1000);
//
//        em.setJpaProperties(jpaProperties);
//        em.afterPropertiesSet();
//
//        return em.getObject();
//    }
//
//    @Bean(name=PREFIX+"TransactionManager")
//    public PlatformTransactionManager transactionManager(@Qualifier(PREFIX+"EntityManagerFactory") EntityManagerFactory entityManagerFactory) {
//        JpaTransactionManager tm = new JpaTransactionManager();
//        tm.setEntityManagerFactory(entityManagerFactory);
//        return tm;
//    }
//}
