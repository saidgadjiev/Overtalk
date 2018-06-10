package ru.saidgadjiev.aboutme.model;

/**
 * Created by said on 23.02.2018.
 */
public class ResponseMessage<T> {

    private String message;

    private T content;

    public ResponseMessage() {
    }

    public ResponseMessage(String message, T content) {
        this.message = message;
        this.content = content;
    }

    public T getContent() {
        return content;
    }

    public ResponseMessage<T> setContent(T content) {
        this.content = content;

        return this;
    }

    public String getMessage() {
        return message;
    }

    public ResponseMessage<T> setMessage(String message) {
        this.message = message;

        return this;
    }
}
