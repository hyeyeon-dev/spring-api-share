package demo.api.core.querydsl;

import org.hibernate.dialect.MySQL57Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;

public class CustomMysqlDialect extends MySQL57Dialect {
    public CustomMysqlDialect() {
        super();

        registerFunction("GROUP_CONCAT", new StandardSQLFunction("group_concat", StandardBasicTypes.STRING));
    }
}
