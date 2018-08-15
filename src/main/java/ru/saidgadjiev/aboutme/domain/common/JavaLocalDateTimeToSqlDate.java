package ru.saidgadjiev.aboutme.domain.common;

import ru.saidgadjiev.ormnext.core.field.datapersister.ColumnConverter;

import java.sql.Date;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class JavaLocalDateTimeToSqlDate implements ColumnConverter<Date, LocalDateTime> {

    @Override
    public Date javaToSql(LocalDateTime value) throws SQLException {
        return new Date(value.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
    }

    @Override
    public LocalDateTime sqlToJava(Date value) throws SQLException {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(value.getTime()), ZoneId.systemDefault());
    }
}
