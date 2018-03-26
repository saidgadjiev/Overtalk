package ru.saidgadjiev.overtalk.application.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import ru.saidgadjiev.overtalk.application.model.ResponseMessage;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by said on 25.03.2018.
 */
public class ResponseUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private ResponseUtils() {

    }

    public static void sendError(HttpServletResponse response, int status, String message) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        ResponseMessage<String> responseMessage = new ResponseMessage<>(status, message);
        PrintWriter writer = response.getWriter();

        writer.write(OBJECT_MAPPER.writeValueAsString(responseMessage));
        writer.flush();
        writer.close();
    }


    public static void sendResponse(HttpServletResponse response, int status, String message, Object object) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        ResponseMessage<?> responseMessage = new ResponseMessage<>(status, message, object);
        PrintWriter writer = response.getWriter();

        writer.write(OBJECT_MAPPER.writeValueAsString(responseMessage));
        writer.flush();
        writer.close();
    }
}
