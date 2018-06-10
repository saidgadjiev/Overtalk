package ru.saidgadjiev.aboutme.domain;

import ru.saidgadjiev.ormnext.core.field.DatabaseColumn;
import ru.saidgadjiev.ormnext.support.datapersister.SerialTypeDataPersister;

public class Role {

    @DatabaseColumn(id = true, generated = true, dataType = SerialTypeDataPersister.SERIAL)
    private Integer id;

    @DatabaseColumn(notNull = true, unique = true)
    private String name;

    public Role() {
    }

    public Role(String name) {
        this.name = name;
    }

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
}
