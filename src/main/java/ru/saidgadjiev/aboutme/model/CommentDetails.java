package ru.saidgadjiev.aboutme.model;

import java.util.Date;

/**
 * Created by said on 04.03.2018.
 */
public class CommentDetails {

    private Integer id;

    private String content;

    private Date createdDate;

    private String nickName;

    private String user;

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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "CommentDetails{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", createdDate=" + createdDate +
                ", nickName='" + nickName + '\'' +
                ", user='" + user + '\'' +
                '}';
    }
}
