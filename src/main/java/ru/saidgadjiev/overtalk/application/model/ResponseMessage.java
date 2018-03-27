package ru.saidgadjiev.overtalk.application.model;

/**
 * Created by said on 23.02.2018.
 */
public class ResponseMessage<T> {

    private String message;

    private T content;

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
