package ru.saidgadjiev.aboutme.domain;

import ru.saidgadjiev.aboutme.dao.SerialTypeDataPersister;
import ru.saidgadjiev.aboutme.dao.TextTypeDataPersister;
import ru.saidgadjiev.aboutme.domain.common.LocalDateTimeToSqlTimeStamp;
import ru.saidgadjiev.ormnext.core.field.*;
import ru.saidgajiev.ormnext.cache.Cacheable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Cacheable
public class Post {

    @DatabaseColumn(id = true, generated = true, persisterClass = SerialTypeDataPersister.class)
    private Integer id;

    @DatabaseColumn(notNull = true)
    private String title;

    @DatabaseColumn(notNull = true, persisterClass = TextTypeDataPersister.class)
    private String content;

    @Converter(value = LocalDateTimeToSqlTimeStamp.class)
    @DatabaseColumn(notNull = true, dataType = DataType.TIMESTAMP)
    private LocalDateTime createdDate = LocalDateTime.now();

    @ForeignCollectionField(foreignFieldName = "post", fetchType = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    @ForeignCollectionField(foreignFieldName = "post")
    private List<Like> likes = new ArrayList<>();

    @ForeignColumn(foreignFieldName = "id", fetchType = FetchType.LAZY, onDelete = ReferenceAction.CASCADE, onUpdate = ReferenceAction.CASCADE)
    private Category category;

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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", createdDate=" + createdDate +
                '}';
    }
}
