package ru.saidgadjiev.aboutme.domain.common;

import ru.saidgadjiev.ormnext.core.field.datapersister.ColumnConverter;

import java.sql.SQLException;
import java.util.Date;

public class JavaDateToSqlTimeStamp implements ColumnConverter<java.sql.Timestamp, Date> {

    @Override
    public java.sql.Timestamp javaToSql(Date value) throws SQLException {
        return new java.sql.Timestamp(value.getTime());
    }

    @Override
    public Date sqlToJava(java.sql.Timestamp value) throws SQLException {
        return new Date(value.getTime());
    }
}
