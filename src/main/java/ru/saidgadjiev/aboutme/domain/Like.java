package ru.saidgadjiev.aboutme.domain;

import ru.saidgadjiev.aboutme.dao.SerialTypeDataPersister;
import ru.saidgadjiev.ormnext.core.field.DatabaseColumn;
import ru.saidgadjiev.ormnext.core.field.FetchType;
import ru.saidgadjiev.ormnext.core.field.ForeignColumn;

public class Like {

    @DatabaseColumn(id = true, generated = true, persisterClass = SerialTypeDataPersister.class)
    private Integer id;

    @ForeignColumn(fetchType = FetchType.LAZY)
    private Post post;

    @ForeignColumn(fetchType = FetchType.LAZY)
    private Comment comment;

    @ForeignColumn(foreignFieldName = "userName", fetchType = FetchType.LAZY)
    private UserProfile2 user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public UserProfile2 getUser() {
        return user;
    }

    public void setUser(UserProfile2 user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Like{" +
                "id=" + id +
                ", post=" + (post == null ? null : post.getId()) +
                ", comment=" + (comment == null ? null : comment.getId()) +
                ", user=" + (user == null ? null : user.getUserName()) +
                '}';
    }
}
