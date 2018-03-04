package ru.saidgadjiev.overtalk.application.model;

/**
 * Created by said on 23.02.2018.
 */
public class ResponseMessage {

    private int code;

    private String message;

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
