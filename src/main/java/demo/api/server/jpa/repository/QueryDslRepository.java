package demo.api.server.jpa.repository;

import demo.api.core.datasource.PrimaryDataSourceConfig;
import demo.api.core.datasource.SecondaryDataSourceConfig;

public abstract class QueryDslRepository {
    final String prefix = PrimaryDataSourceConfig.PREFIX;
}
