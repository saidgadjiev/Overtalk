package ru.saidgadjiev.aboutme.domain;

import ru.saidgadjiev.aboutme.domain.common.JavaDateToSqlDate;
import ru.saidgadjiev.ormnext.core.field.*;
import ru.saidgadjiev.ormnext.support.datapersister.SerialTypeDataPersister;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by said on 19.02.2018.
 */

public class Post {

    @DatabaseColumn(id = true, generated = true, dataType = SerialTypeDataPersister.SERIAL)
    private int id;

    @DatabaseColumn(notNull = true)
    private String title;

    @DatabaseColumn(notNull = true)
    private String content;

    @Converter(value = JavaDateToSqlDate.class)
    @DatabaseColumn(notNull = true)
    private Date createdDate = new Date();

    @ForeignColumn
    private UserProfile user;

    @ForeignCollectionField(foreignFieldName = "post", fetchType = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

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

    public UserProfile getUser() {
        return user;
    }

    public void setUser(UserProfile user) {
        this.user = user;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Post{" +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", createdDate='" + createdDate + "'\'" +
                '}';
    }
}
