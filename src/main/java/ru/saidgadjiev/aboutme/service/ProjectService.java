package ru.saidgadjiev.aboutme.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.saidgadjiev.aboutme.dao.ProjectDao;
import ru.saidgadjiev.aboutme.domain.Project;
import ru.saidgadjiev.aboutme.model.ProjectDetails;
import ru.saidgadjiev.aboutme.utils.DTOUtils;

import java.sql.SQLException;
import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectDao dao;

    public List<ProjectDetails> getAll() throws SQLException {
        return DTOUtils.convert(dao.getAll(), ProjectDetails.class);
    }

    public void create(ProjectDetails projectDetails) throws SQLException {
        dao.create(DTOUtils.convert(projectDetails, Project.class));
    }
}
