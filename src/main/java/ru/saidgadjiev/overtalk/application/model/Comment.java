package ru.saidgadjiev.overtalk.application.model;

import ru.saidgadjiev.orm.next.core.field.DBField;
import ru.saidgadjiev.orm.next.core.field.DataType;
import ru.saidgadjiev.orm.next.core.field.Getter;
import ru.saidgadjiev.orm.next.core.field.Setter;
import ru.saidgadjiev.orm.next.core.table.DBTable;

/**
 * Created by said on 12.02.2018.
 */
@DBTable(name = "comment")
public class Comment {

    @Getter(name = "getId")
    @Setter(name = "setId")
    @DBField(id = true, generated = true, dataType = DataType.LONG)
    private int id;

    @Getter(name = "getContent")
    @Setter(name = "setContent")
    @DBField(dataType = DataType.STRING)
    private String content;

    public Comment() {
    }

    public Comment(int id, String content) {
        this.id = id;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
