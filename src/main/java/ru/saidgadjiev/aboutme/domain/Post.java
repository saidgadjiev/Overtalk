package ru.saidgadjiev.aboutme.domain;

import ru.saidgadjiev.aboutme.dao.SerialTypeDataPersister;
import ru.saidgadjiev.aboutme.domain.common.JavaDateToSqlDate;
import ru.saidgadjiev.ormnext.core.field.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by said on 19.02.2018.
 */

public class Post {

    @DatabaseColumn(id = true, generated = true, persisterClass = SerialTypeDataPersister.class)
    private Integer id;

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

    @ForeignCollectionField(foreignFieldName = "post")
    private List<Like> likes = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
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
