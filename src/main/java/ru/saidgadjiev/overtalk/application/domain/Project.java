package ru.saidgadjiev.overtalk.application.domain;

import ru.saidgadjiev.orm.next.core.field.DBField;
import ru.saidgadjiev.orm.next.core.field.DataType;
import ru.saidgadjiev.orm.next.core.field.Getter;
import ru.saidgadjiev.orm.next.core.field.Setter;
import ru.saidgadjiev.orm.next.core.table.DBTable;
import ru.saidgadjiev.overtalk.application.common.Constants;

@DBTable
public class Project {

    @Getter(name = "getId")
    @Setter(name = "setId")
    @DBField(id = true, generated = true, dataType = Constants.PK_TYPE)
    private int id;

    @Getter(name = "getTitle")
    @Setter(name = "setTitle")
    @DBField(dataType = DataType.STRING)
    private String title;

    @Getter(name = "getDescription")
    @Setter(name = "setDescription")
    @DBField(dataType = Constants.TEXT_TYPE)
    private String description;

    @Getter(name = "getImagePath")
    @Setter(name = "setImagePath")
    @DBField(dataType = DataType.STRING)
    private String imagePath;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
