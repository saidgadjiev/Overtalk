package ru.saidgadjiev.aboutme.domain.common;

import ru.saidgadjiev.ormnext.core.field.datapersister.ColumnConverter;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

public class JavaLocalDateToSqlDate implements ColumnConverter<Date, LocalDate> {

    @Override
    public Date javaToSql(LocalDate value) throws SQLException {
        return Date.valueOf(value);
    }

    @Override
    public LocalDate sqlToJava(Date value) throws SQLException {
        return value.toLocalDate();
    }
}
