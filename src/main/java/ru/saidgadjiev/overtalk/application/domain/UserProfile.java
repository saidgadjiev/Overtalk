package ru.saidgadjiev.overtalk.application.domain;

import ru.saidgadjiev.orm.next.core.field.*;
import ru.saidgadjiev.orm.next.core.table.DBTable;
import ru.saidgadjiev.orm.next.core.table.Unique;
import ru.saidgadjiev.overtalk.application.common.Constants;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by said on 18.03.2018.
 */
@DBTable(
        name = "user",
        uniqueConstraints = {
                @Unique(columns = {"userName"})
        }
)
public class UserProfile {

    @Getter(name = "getId")
    @Setter(name = "setId")
    @DBField(id = true, generated = true, dataType = Constants.PK_TYPE)
    private Integer id;

    @Getter(name = "getUserName")
    @Setter(name = "setUserName")
    @DBField(dataType = DataType.STRING, notNull = true)
    private String userName;

    @Getter(name = "getNickName")
    @Setter(name = "setNickName")
    @DBField(dataType = DataType.STRING, notNull = true)
    private String nickName;

    @Getter(name = "getPassword")
    @Setter(name = "setPassword")
    @DBField(dataType = DataType.STRING, notNull = true)
    private String password;

    @Getter(name = "getUserRoles")
    @Setter(name = "setUserRoles")
    @ForeignCollectionField(foreignFieldName = "user", fetchType = FetchType.LAZY)
    private Set<UserRole> userRoles = new HashSet<>();

    @Getter(name = "getComments")
    @Setter(name = "setComments")
    @ForeignCollectionField(foreignFieldName = "user", fetchType = FetchType.LAZY)
    private Set<Comment> comments = new HashSet<>();

    @Getter(name = "getPosts")
    @Setter(name = "setPosts")
    @ForeignCollectionField(foreignFieldName = "user", fetchType = FetchType.LAZY)
    private Set<Post> posts = new HashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Set<UserRole> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(Set<UserRole> userRoles) {
        this.userRoles = userRoles;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }
}
