package demo.api.server.jpa.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import demo.api.core.datasource.PrimaryDataSourceConfig;
import demo.api.server.jpa.QRequestClientEntity;
import demo.api.server.jpa.entity.RequestClientEntity;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class AccessLogQueryRepository extends QueryDslRepository {
    final String primaryDatasourcePrefix = PrimaryDataSourceConfig.PREFIX;

    @Resource
    @Qualifier(primaryDatasourcePrefix+"JpaQueryFactory")
    private JPAQueryFactory primaryQueryFactory;

    @PersistenceContext(unitName = primaryDatasourcePrefix+"EntityManager")
    private EntityManager primaryEntityManager;

    @Resource
    @Qualifier(prefix+"JpaQueryFactory")
    private JPAQueryFactory queryFactory;

    @PersistenceContext(unitName = prefix+"EntityManager")
    private EntityManager entityManager;

    public List<RequestClientEntity> findAll() {
        QRequestClientEntity qRequestClientEntity = QRequestClientEntity.requestClientEntity;
        return primaryQueryFactory.selectFrom(qRequestClientEntity).fetch();
    }
}
