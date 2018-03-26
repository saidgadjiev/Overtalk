package ru.saidgadjiev.overtalk.application.dao;

import ru.saidgadjiev.orm.next.core.field.persisters.IntegerDataPersister;

public class SerialTypeDataPersister extends IntegerDataPersister {

    @Override
    public int getDataType() {
        return 8;
    }
}
