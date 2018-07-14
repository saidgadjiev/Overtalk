package ru.saidgadjiev.aboutme.domain;

import ru.saidgadjiev.aboutme.dao.SerialTypeDataPersister;
import ru.saidgadjiev.aboutme.dao.TextTypeDataPersister;
import ru.saidgadjiev.aboutme.domain.common.JavaDateToSqlDate;
import ru.saidgadjiev.ormnext.core.field.*;

import java.util.Date;


public class Comment {

    @DatabaseColumn(id = true, generated = true, persisterClass = SerialTypeDataPersister.class)
    private Integer id;

    @DatabaseColumn(notNull = true, persisterClass = TextTypeDataPersister.class)
    private String content;

    @DatabaseColumn(notNull = true)
    @ForeignColumn(fetchType = FetchType.LAZY, onDelete = ReferenceAction.CASCADE, onUpdate = ReferenceAction.CASCADE)
    private Post post;

    @Converter(value = JavaDateToSqlDate.class)
    @DatabaseColumn
    private Date createdDate = new Date();

    @DatabaseColumn(notNull = true)
    @ForeignColumn(foreignFieldName = "userName")
    private UserProfile user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
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
}
