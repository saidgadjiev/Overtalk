package ru.saidgadjiev.overtalk.application.domain;

import ru.saidgadjiev.orm.next.core.field.DBField;
import ru.saidgadjiev.orm.next.core.field.DataType;
import ru.saidgadjiev.orm.next.core.field.Getter;
import ru.saidgadjiev.orm.next.core.field.Setter;
import ru.saidgadjiev.orm.next.core.table.DBTable;
import ru.saidgadjiev.orm.next.core.table.Unique;
import ru.saidgadjiev.overtalk.application.configuration.Constants;

/**
 * Created by said on 18.03.2018.
 */
@DBTable(name = "role",
        uniqueConstraints = {
                @Unique(columns = {"name"})
        }
)
public class Role {

    @Getter(name = "getId")
    @Setter(name = "setId")
    @DBField(id = true, generated = true, dataType = Constants.PK_TYPE)
    private Integer id;

    @Getter(name = "getName")
    @Setter(name = "setName")
    @DBField(dataType = DataType.STRING, notNull = true)
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
