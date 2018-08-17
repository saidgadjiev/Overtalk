package ru.saidgadjiev.aboutme;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ru.saidgadjiev.aboutme.model.CommentDetails;
import ru.saidgadjiev.aboutme.model.JsonViews;

import java.time.LocalDateTime;
import java.util.Date;

public class Test {

    @org.junit.Test
    public void test() throws Exception {
        CommentDetails item = new CommentDetails();

        item.setCreatedDate(LocalDateTime.now());
        item.setDate(new Date());
        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String result = mapper
                .writeValueAsString(item);

        System.out.println(result);
    }
}
