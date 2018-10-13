package ru.saidgadjiev.aboutme.domain;

import ru.saidgadjiev.aboutme.dao.SerialTypeDataPersister;
import ru.saidgadjiev.ormnext.core.field.DatabaseColumn;
import ru.saidgadjiev.ormnext.core.field.FetchType;
import ru.saidgadjiev.ormnext.core.field.ForeignColumn;
import ru.saidgadjiev.ormnext.core.table.DatabaseEntity;
import ru.saidgadjiev.ormnext.core.table.Unique;
import ru.saidgajiev.ormnext.cache.Cacheable;

@Cacheable
@DatabaseEntity(uniqueConstraints = {
        @Unique(columns = {"post", "user"})
})
public class Like {

    @DatabaseColumn(id = true, generated = true, persisterClass = SerialTypeDataPersister.class)
    private Integer id;

    @ForeignColumn(fetchType = FetchType.LAZY)
    private Post post;

    @ForeignColumn(fetchType = FetchType.LAZY)
    private Comment comment;

    @DatabaseColumn(columnName = "userprofile_username", notNull = true)
    @ForeignColumn(foreignFieldName = "username", fetchType = FetchType.LAZY)
    private Userprofile user;

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

    public Userprofile getUser() {
        return user;
    }

    public void setUser(Userprofile user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Like{" +
                "id=" + id +
                '}';
    }
}
