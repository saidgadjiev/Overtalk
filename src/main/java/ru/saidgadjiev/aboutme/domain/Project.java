package ru.saidgadjiev.aboutme.domain;

import ru.saidgadjiev.aboutme.domain.common.JavaDateToSqlDate;
import ru.saidgadjiev.ormnext.core.field.Converter;
import ru.saidgadjiev.ormnext.core.field.DatabaseColumn;
import ru.saidgadjiev.ormnext.support.datapersister.SerialTypeDataPersister;

import java.util.Date;

public class Project {

    @DatabaseColumn(id = true, generated = true, dataType = SerialTypeDataPersister.SERIAL)
    private int id;

    @DatabaseColumn(notNull = true, unique = true)
    private String name;

    @DatabaseColumn(notNull = true)
    private String description;

    @DatabaseColumn(notNull = true, defaultDefinition = "'fake.png'")
    private String logoPath;

    @Converter(value = JavaDateToSqlDate.class)
    @DatabaseColumn(notNull = true)
    private Date createdDate = new Date();

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
