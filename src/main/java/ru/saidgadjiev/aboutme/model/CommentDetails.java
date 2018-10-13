package ru.saidgadjiev.aboutme.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.format.annotation.DateTimeFormat;
import ru.saidgadjiev.aboutme.json.LocalDateTimeSerializerddMMyyyyHHmm;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by said on 04.03.2018.
 */
public class CommentDetails {

    @JsonView(JsonViews.Ui.class)
    private Integer id;

    @JsonView(JsonViews.Rest.class)
    @NotNull
    @Size(min = 1)
    private String content;

    @JsonSerialize(using = LocalDateTimeSerializerddMMyyyyHHmm.class)
    private LocalDateTime createdDate;

    @JsonView(JsonViews.Ui.class)
    private String nickname;

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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "CommentDetails{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", createdDate=" + createdDate +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
