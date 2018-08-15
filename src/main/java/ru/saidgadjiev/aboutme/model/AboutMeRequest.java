package ru.saidgadjiev.aboutme.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by said on 14.08.2018.
 */
public class AboutMeRequest {

    @NotNull
    @Size(min = 1)
    private String post;

    @NotNull
    @Size(min = 1)
    private String placeOfResidence;

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getPlaceOfResidence() {
        return placeOfResidence;
    }

    public void setPlaceOfResidence(String placeOfResidence) {
        this.placeOfResidence = placeOfResidence;
    }
}
