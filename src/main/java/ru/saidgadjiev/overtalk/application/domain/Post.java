package ru.saidgadjiev.overtalk.application.domain;

import ru.saidgadjiev.orm.next.core.field.*;
import ru.saidgadjiev.orm.next.core.table.DBTable;
import ru.saidgadjiev.overtalk.application.common.Constants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by said on 19.02.2018.
 */

@DBTable(name = "post")
public class Post {

    @Getter(name = "getId")
    @Setter(name = "setId")
    @DBField(id = true, generated = true, dataType = Constants.PK_TYPE)
    private int id;

    @Getter(name = "getTitle")
    @Setter(name = "setTitle")
    @DBField(dataType = DataType.STRING, notNull = true)
    private String title;

    @Getter(name = "getContent")
    @Setter(name = "setContent")
    @DBField(dataType = DataType.STRING, notNull = true)
    private String content;

    @Getter(name = "getCreatedDate")
    @Setter(name = "setCreatedDate")
    @DBField(dataType = DataType.DATE, notNull = true)
    private Date createdDate = new Date();

    @Getter(name = "getComments")
    @Setter(name = "setComments")
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
