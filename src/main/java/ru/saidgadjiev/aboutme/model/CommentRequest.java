package ru.saidgadjiev.aboutme.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by said on 04.03.2018.
 */
public class CommentRequest {

    @NotNull
    @Size(min = 1)
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "CommentRequest{" +
                ", content='" + content + '\'' +
                '}';
    }
}
