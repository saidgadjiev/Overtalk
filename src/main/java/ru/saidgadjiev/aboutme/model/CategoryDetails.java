package ru.saidgadjiev.aboutme.model;

import com.fasterxml.jackson.annotation.JsonView;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by said on 14.08.2018.
 */
public class CategoryDetails {

    @JsonView(JsonViews.Ui.class)
    private int id;

    @JsonView(JsonViews.Ui.class)
    private int postsCount;

    @JsonView({JsonViews.Rest.class, JsonViews.Ui.class})
    @NotNull
    @Size(min = 1)
    private String name;

    @JsonView({JsonViews.Rest.class, JsonViews.Ui.class})
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPostsCount() {
        return postsCount;
    }

    public void setPostsCount(int postsCount) {
        this.postsCount = postsCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
