package ru.saidgadjiev.aboutme.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.saidgadjiev.aboutme.dao.ProjectDao;
import ru.saidgadjiev.aboutme.domain.Project;
import ru.saidgadjiev.aboutme.utils.DTOUtils;

import java.sql.SQLException;
import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectDao dao;

    public List<Project> getAll() throws SQLException {
        return DTOUtils.convert(dao.getAll(), Project.class);
    }

    public void create(Project project) throws SQLException {
        dao.create(project);
    }

    public int update(Project project) throws SQLException {
        return dao.update(project);
    }
}
