package demo.api.core.querydsl;

import demo.api.core.datasource.PrimaryDataSourceConfig;
import demo.api.core.datasource.SecondaryDataSourceConfig;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Configuration
public class QuerydslConfig {
    final String prefixPrimary = PrimaryDataSourceConfig.PREFIX;
    final String prefixSecondary = SecondaryDataSourceConfig.PREFIX;
//    final String prefixThird = ThirdDataSourceConfig.PREFIX;

    @PersistenceContext(unitName = prefixPrimary+"EntityManager")
    private EntityManager primaryEntityManager;

    @Bean(name = prefixPrimary+"JpaQueryFactory")
    public JPAQueryFactory primaryJpaQueryFactory() {
        return new JPAQueryFactory(primaryEntityManager);
    }

    @PersistenceContext(unitName = prefixSecondary+"EntityManager")
    private EntityManager secondaryEntityManager;

    @Bean(name = prefixSecondary+"JpaQueryFactory")
    public JPAQueryFactory secondaryJpaQueryFactory() {
        return new JPAQueryFactory(secondaryEntityManager);
    }

//    @PersistenceContext(unitName = prefixThird+"EntityManager")
//    private EntityManager thirdEntityManager;
//
//    @Bean(name = prefixThird+"JpaQueryFactory")
//    public JPAQueryFactory thirdJpaQueryFactory() {
//        return new JPAQueryFactory(thirdEntityManager);
//    }

}
