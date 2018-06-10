package ru.saidgadjiev.aboutme.domain;

import ru.saidgadjiev.aboutme.domain.common.JavaDateToSqlDate;
import ru.saidgadjiev.ormnext.core.field.Converter;
import ru.saidgadjiev.ormnext.core.field.DatabaseColumn;
import ru.saidgadjiev.ormnext.core.field.FetchType;
import ru.saidgadjiev.ormnext.core.field.ForeignColumn;
import ru.saidgadjiev.ormnext.support.datapersister.SerialTypeDataPersister;

import java.util.Date;

public class Comment {

    @DatabaseColumn(id = true, generated = true, dataType = SerialTypeDataPersister.SERIAL)
    private int id;

    @DatabaseColumn
    private String content;

    @ForeignColumn(fetchType = FetchType.LAZY)
    private Post post;

    @Converter(value = JavaDateToSqlDate.class)
    @DatabaseColumn
    private Date createdDate = new Date();

    @ForeignColumn
    private UserProfile user;

    public Comment() { }

    public Comment(int id, String content) {
        this.id = id;
        this.content = content;
    }

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
