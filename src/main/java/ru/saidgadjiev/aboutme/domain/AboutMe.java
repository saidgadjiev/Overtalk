package ru.saidgadjiev.aboutme.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ru.saidgadjiev.aboutme.domain.common.LocalDateToSqlTimeStamp;
import ru.saidgadjiev.aboutme.json.LocalDateSerializerddMMyyyy;
import ru.saidgadjiev.ormnext.core.field.Converter;
import ru.saidgadjiev.ormnext.core.field.DataType;
import ru.saidgadjiev.ormnext.core.field.DatabaseColumn;
import ru.saidgadjiev.ormnext.core.field.ForeignCollectionField;
import ru.saidgajiev.ormnext.cache.Cacheable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Cacheable
public class AboutMe {

    @DatabaseColumn(id = true)
    private int id;

    @DatabaseColumn(notNull = true)
    private String fio;

    @Converter(value = LocalDateToSqlTimeStamp.class)
    @DatabaseColumn(notNull = true, dataType = DataType.TIMESTAMP)
    @JsonSerialize(using = LocalDateSerializerddMMyyyy.class)
    private LocalDate dateOfBirth;

    @DatabaseColumn(notNull = true)
    private String placeOfResidence;

    @DatabaseColumn(notNull = true)
    private String educationLevel;

    @DatabaseColumn(notNull = true)
    private String post;

    @DatabaseColumn
    private String additionalInformation;

    @ForeignCollectionField(foreignFieldName = "aboutMe")
    private List<Skill> skills = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPlaceOfResidence() {
        return placeOfResidence;
    }

    public void setPlaceOfResidence(String placeOfResidence) {
        this.placeOfResidence = placeOfResidence;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public String getEducationLevel() {
        return educationLevel;
    }

    public void setEducationLevel(String educationLevel) {
        this.educationLevel = educationLevel;
    }

    @Override
    public String toString() {
        return "AboutMe{" +
                "id=" + id +
                ", fio='" + fio + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", placeOfResidence='" + placeOfResidence + '\'' +
                ", post='" + post + '\'' +
                ", additionalInformation='" + additionalInformation + '\'' +
                '}';
    }
}
