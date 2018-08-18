package ru.saidgadjiev.aboutme.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by said on 17.08.2018.
 */
public class LocalDateSerializerddMMyyyy extends StdSerializer<LocalDate> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public LocalDateSerializerddMMyyyy() {
        this(null);
    }

    public LocalDateSerializerddMMyyyy(Class<LocalDate> t) {
        super(t);
    }

    @Override
    public void serialize(LocalDate localDate,
                          JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider
    ) throws IOException {
        jsonGenerator.writeString(FORMATTER.format(localDate));
    }
}
