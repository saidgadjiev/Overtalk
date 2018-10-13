package ru.saidgadjiev.aboutme.domain;

import com.fasterxml.jackson.annotation.JsonView;
import ru.saidgadjiev.aboutme.dao.SerialTypeDataPersister;
import ru.saidgadjiev.aboutme.dao.TextTypeDataPersister;
import ru.saidgadjiev.aboutme.model.JsonViews;
import ru.saidgadjiev.ormnext.core.field.DatabaseColumn;
import ru.saidgajiev.ormnext.cache.Cacheable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Cacheable
public class Project {

    @DatabaseColumn(id = true, generated = true, persisterClass = SerialTypeDataPersister.class)
    private Integer id;

    @NotNull
    @Size(min = 1)
    @DatabaseColumn(notNull = true, unique = true)
    private String name;

    @NotNull
    @Size(min = 1)
    @DatabaseColumn(persisterClass = TextTypeDataPersister.class)
    private String description;

    @DatabaseColumn
    private String logoPath;

    @DatabaseColumn(length = 1024)
    private String projectLink;

    @DatabaseColumn(persisterClass = TextTypeDataPersister.class)
    private String technologies;

    @DatabaseColumn(persisterClass = TextTypeDataPersister.class)
    private String features;

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

    public String getProjectLink() {
        return projectLink;
    }

    public void setProjectLink(String projectLink) {
        this.projectLink = projectLink;
    }

    public String getTechnologies() {
        return technologies;
    }

    public void setTechnologies(String technologies) {
        this.technologies = technologies;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", logoPath='" + logoPath + '\'' +
                ", projectLink='" + projectLink + '\'' +
                '}';
    }
}
