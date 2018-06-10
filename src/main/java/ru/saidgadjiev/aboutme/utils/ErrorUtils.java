package ru.saidgadjiev.aboutme.utils;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import ru.saidgadjiev.aboutme.model.Error;

import java.util.ArrayList;
import java.util.List;

public class ErrorUtils {

    public static List<Error> toErrors(BindingResult bindingResult) {
        List<Error> errors = new ArrayList<>();

        for (FieldError fieldError: bindingResult.getFieldErrors()) {
            errors.add(new Error(fieldError.getField(), fieldError.getDefaultMessage()));
        }

        return errors;
    }
}
