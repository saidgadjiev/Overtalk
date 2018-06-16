package ru.saidgadjiev.aboutme.domain;

import ru.saidgadjiev.aboutme.dao.SerialTypeDataPersister;
import ru.saidgadjiev.aboutme.domain.common.JavaDateToSqlDate;
import ru.saidgadjiev.ormnext.core.field.Converter;
import ru.saidgadjiev.ormnext.core.field.DatabaseColumn;

import java.util.Date;

public class Project {

    @DatabaseColumn(id = true, generated = true, persisterClass = SerialTypeDataPersister.class)
    private Integer id;

    @DatabaseColumn(notNull = true, unique = true)
    private String name;

    @DatabaseColumn(notNull = true)
    private String description;

    @DatabaseColumn(notNull = true, defaultDefinition = "'fake.png'")
    private String logoPath;

    @Converter(value = JavaDateToSqlDate.class)
    @DatabaseColumn(notNull = true)
    private Date createdDate = new Date();

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
}
