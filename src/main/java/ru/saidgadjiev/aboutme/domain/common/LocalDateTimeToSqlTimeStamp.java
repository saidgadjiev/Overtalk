package ru.saidgadjiev.aboutme.domain.common;

import ru.saidgadjiev.ormnext.core.field.datapersister.ColumnConverter;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class LocalDateTimeToSqlTimeStamp implements ColumnConverter<Timestamp, LocalDateTime> {

    @Override
    public Timestamp javaToSql(LocalDateTime value) throws SQLException {
        return Timestamp.valueOf(value);
    }

    @Override
    public LocalDateTime sqlToJava(Timestamp value) throws SQLException {
        return value.toLocalDateTime();
    }
}
