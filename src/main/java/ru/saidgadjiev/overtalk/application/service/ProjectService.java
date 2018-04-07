package ru.saidgadjiev.overtalk.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.saidgadjiev.overtalk.application.dao.ProjectDao;
import ru.saidgadjiev.overtalk.application.model.ProjectDetails;
import ru.saidgadjiev.overtalk.application.utils.DTOUtils;

import java.sql.SQLException;
import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectDao dao;

    public List<ProjectDetails> getAll() throws SQLException {
        return DTOUtils.convert(dao.getAll(), ProjectDetails.class);
    }
}
