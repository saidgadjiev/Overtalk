package ru.saidgadjiev.aboutme.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class CategoryDetails {

    private Integer id;

    @NotNull
    @Min(1)
    private String name;

    @NotNull
    @Min(1)
    private String description;

    private int postsCount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public int getPostsCount() {
        return postsCount;
    }

    public void setPostsCount(int postsCount) {
        this.postsCount = postsCount;
    }
}
