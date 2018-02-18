package ru.saidgadjiev.overtalk.application.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.saidgadjiev.overtalk.application.model.Comment;

/**
 * Created by said on 12.02.2018.
 */
@RestController
@RequestMapping(value = "hello")
public class HelloWorldController {

    @RequestMapping(value = "/user")
    public Comment hello() {
        return new Comment(1, "Test");
    }
}
