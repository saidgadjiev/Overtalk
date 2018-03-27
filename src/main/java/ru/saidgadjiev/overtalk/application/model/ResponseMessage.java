package ru.saidgadjiev.overtalk.application.model;

/**
 * Created by said on 23.02.2018.
 */
public class ResponseMessage<T> {

    private int code;

    private T content;

    public ResponseMessage(int code, T content) {
        this.code = code;
        this.content = content;
    }

    public ResponseMessage(int code) {
        this.code = code;
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
}
