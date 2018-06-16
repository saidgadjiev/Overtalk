package ru.saidgadjiev.aboutme.model;

import java.util.Date;

public class ProjectDetails {

    private Integer id;

    private String name;

    private String description;

    private String logoPath;

    private Date createdDate;

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

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "ProjectDetails{" +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", logoPath='" + logoPath + '\'' +
                ", createdDate=" + createdDate +
                '}';
    }
}
