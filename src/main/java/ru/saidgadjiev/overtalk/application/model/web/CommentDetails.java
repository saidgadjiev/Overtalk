package ru.saidgadjiev.overtalk.application.model.web;

import java.util.Date;

/**
 * Created by said on 04.03.2018.
 */
public class CommentDetails {

    private int id;

    private String content;

    private Date createdDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
