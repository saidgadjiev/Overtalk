package ru.saidgadjiev.aboutme.domain.common;

import ru.saidgadjiev.ormnext.core.field.datapersister.ColumnConverter;

import java.sql.SQLException;
import java.util.Date;

public class JavaDateToSqlDate implements ColumnConverter<java.sql.Date, Date> {

    @Override
    public java.sql.Date javaToSql(Date value) throws SQLException {
        return new java.sql.Date(value.getTime());
    }

    @Override
    public Date sqlToJava(java.sql.Date value) throws SQLException {
        return new Date(value.getTime());
    }
}
