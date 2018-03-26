package ru.saidgadjiev.overtalk.application.model;

/**
 * Created by said on 23.02.2018.
 */
public class ResponseMessage<T> {

    private int code;

    private String message;

    private T content;

    public ResponseMessage(int code, String message, T content) {
        this.code = code;
        this.message = message;
        this.content = content;
    }

    public ResponseMessage(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
