package ru.saidgadjiev.aboutme.domain.common;

import ru.saidgadjiev.ormnext.core.field.datapersister.ColumnConverter;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;

public class LocalDateToSqlTimeStamp implements ColumnConverter<Timestamp, LocalDate> {

    @Override
    public Timestamp javaToSql(LocalDate value) throws SQLException {
        return Timestamp.valueOf(value.atStartOfDay());
    }

    @Override
    public LocalDate sqlToJava(Timestamp value) throws SQLException {
        return value.toLocalDateTime().toLocalDate();
    }
}
