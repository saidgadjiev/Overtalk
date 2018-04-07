package ru.saidgadjiev.overtalk.application.dao;

import ru.saidgadjiev.orm.next.core.db.DatabaseType;
import ru.saidgadjiev.orm.next.core.query.core.AttributeDefinition;
import ru.saidgadjiev.overtalk.application.common.Constants;

public class PGDatabaseType implements DatabaseType {
    @Override
    public String appendPrimaryKey(boolean generated) {
        StringBuilder builder = new StringBuilder();

        builder.append(" PRIMARY KEY");

        return builder.toString();
    }

    @Override
    public String getDatabaseName() {
        return "PostgreSQL";
    }

    @Override
    public String getDriverClassName() {
        return "org.postgresql.Driver";
    }

    @Override
    public String appendNoColumn() {
        return "() VALUES ()";
    }

    @Override
    public String getEntityNameEscape() {
        return "\"";
    }

    @Override
    public String typeToSql(int type, AttributeDefinition attributeDefinition) {
        if (type == Constants.PK_TYPE) {
            return "SERIAL";
        }
        if (type == Constants.TEXT_TYPE) {
            return "TEXT";
        }

        return "";
    }
}
