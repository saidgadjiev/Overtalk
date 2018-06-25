package ru.saidgadjiev.aboutme.domain;

import ru.saidgadjiev.aboutme.dao.TextTypeDataPersister;
import ru.saidgadjiev.ormnext.core.field.DatabaseColumn;
import ru.saidgadjiev.ormnext.core.field.ForeignCollectionField;

import java.util.ArrayList;
import java.util.List;

public class AboutMe {

    @DatabaseColumn(id = true)
    private int id;

    @DatabaseColumn(notNull = true, persisterClass = TextTypeDataPersister.class)
    private String biography;

    @ForeignCollectionField(foreignFieldName = "aboutMe")
    private List<Skill> skills = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    @Override
    public String toString() {
        return "AboutMe{" +
                "id=" + id +
                ", biography='" + biography + '\'' +
                ", skills=" + skills +
                '}';
    }
}
