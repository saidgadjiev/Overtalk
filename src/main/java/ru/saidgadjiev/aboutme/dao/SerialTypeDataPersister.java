package ru.saidgadjiev.aboutme.dao;

import ru.saidgadjiev.ormnext.core.dialect.BaseDialect;
import ru.saidgadjiev.ormnext.core.field.SqlType;
import ru.saidgadjiev.ormnext.core.field.datapersister.IntegerDataPersister;
import ru.saidgadjiev.ormnext.core.query.visitor.element.AttributeDefinition;

public class SerialTypeDataPersister extends IntegerDataPersister {

    @Override
    public SqlType getOrmNextSqlType() {
        return SqlType.OTHER;
    }

    @Override
    public SqlType getForeignOrmNextSqlType() {
        return SqlType.INTEGER;
    }

    @Override
    public String getOtherTypeSql(BaseDialect baseDialect, AttributeDefinition def) {
        return "SERIAL";
    }
}
