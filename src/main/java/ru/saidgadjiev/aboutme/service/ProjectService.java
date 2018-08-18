package ru.saidgadjiev.aboutme.service;

import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.saidgadjiev.aboutme.dao.ProjectDao;
import ru.saidgadjiev.aboutme.domain.Project;
import ru.saidgadjiev.aboutme.model.ProjectDetails;

import java.sql.SQLException;
import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectDao dao;

    public List<Project> getAll() throws SQLException {
        return dao.getAll();
    }

    public Project create(ProjectDetails projectDetails) throws SQLException {
        Project project = new Project();

        project.setName(projectDetails.getName());
        project.setProjectLink(projectDetails.getProjectLink());
        project.setDescription(projectDetails.getDescription());
        project.setLogoPath(projectDetails.getLogoPath());

        dao.create(project);

        return project;
    }


    @Nullable
    public Project update(Integer id, ProjectDetails projectDetails) throws SQLException {
        Project project = dao.getById(id);

        if (project == null) {
            return null;
        }

        project.setName(projectDetails.getName());
        project.setDescription(projectDetails.getDescription());
        project.setLogoPath(projectDetails.getLogoPath());
        project.setProjectLink(projectDetails.getProjectLink());

        dao.update(id, project);

        return project;
    }
}
