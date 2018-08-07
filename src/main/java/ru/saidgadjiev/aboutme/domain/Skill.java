package ru.saidgadjiev.aboutme.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.saidgadjiev.aboutme.dao.SerialTypeDataPersister;
import ru.saidgadjiev.ormnext.core.field.DatabaseColumn;
import ru.saidgadjiev.ormnext.core.field.ForeignColumn;
import ru.saidgajiev.ormnext.cache.Cacheable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Cacheable
public class Skill {

    @DatabaseColumn(id = true, generated = true, persisterClass = SerialTypeDataPersister.class)
    private Integer id;

    @NotNull
    @Size(min = 1)
    @DatabaseColumn(notNull = true)
    private String name;

    @DatabaseColumn(defaultDefinition = "0")
    private int percentage;

    @JsonIgnore
    @DatabaseColumn(notNull = true)
    @ForeignColumn
    private AboutMe aboutMe;

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

    public AboutMe getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(AboutMe aboutMe) {
        this.aboutMe = aboutMe;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    @Override
    public String toString() {
        return "Skill{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", percentage=" + percentage +
                ", aboutMe=" + aboutMe +
                '}';
    }
}
