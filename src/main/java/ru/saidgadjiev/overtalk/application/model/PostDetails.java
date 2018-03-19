package ru.saidgadjiev.overtalk.application.model;

import java.util.Date;
import java.util.List;

/**
 * Created by said on 04.03.2018.
 */
public class PostDetails {

    private int id;

    private String title;

    private String content;

    private Date createdDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    @Override
    public String toString() {
        return "PostDetails{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
