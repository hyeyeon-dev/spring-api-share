package demo.api.core.datasource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.hibernate.cfg.Environment;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@MapperScan(basePackages = {"demo.api.mapper.primary"}, sqlSessionFactoryRef = "pSqlSessionFactory")
@EnableJpaRepositories(
        basePackages = {"demo.api.jpa.repository"},
        entityManagerFactoryRef = "pEntityManagerFactory",
        transactionManagerRef = "pTransactionManager"
)
public class PrimaryDataSourceConfig {
    public static final String PREFIX = "p";

    @Bean(name=PREFIX+"DataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource-primary")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name=PREFIX+"EntityManagerFactory")
    public EntityManagerFactory entityManagerFactory(@Qualifier(PREFIX+"DataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("demo.api.jpa.entity");
        em.setPersistenceUnitName(PREFIX+"EntityManager");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        Properties jpaProperties = new Properties();
        jpaProperties.put(Environment.SHOW_SQL, false);
        jpaProperties.put(Environment.HBM2DDL_AUTO, "none");
        jpaProperties.put(Environment.FORMAT_SQL, false);
        jpaProperties.put(Environment.DEFAULT_BATCH_FETCH_SIZE, 1000);

        em.setJpaProperties(jpaProperties);
        em.afterPropertiesSet();

        return em.getObject();
    }

    @Primary
    @Bean(name=PREFIX+"TransactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier(PREFIX+"EntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager tm = new JpaTransactionManager();
        tm.setEntityManagerFactory(entityManagerFactory);
        return tm;
    }

    @Bean(name=PREFIX+"SqlSessionFactory")
    @Primary
    public SqlSessionFactory sqlSessionFactory(@Qualifier(PREFIX+"DataSource") DataSource dataSource, ApplicationContext applicationContext) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setConfigLocation(applicationContext.getResource("classpath:mybatis/mybatis-config.xml"));
        sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:mybatis/mapper/primary/*.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name=PREFIX+"SqlSessionTemplate")
    @Primary
    public SqlSessionTemplate  sqlSessionTemplate(@Qualifier(PREFIX+"SqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
