package ru.saidgadjiev.aboutme.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.saidgadjiev.aboutme.dao.AboutMeDao;
import ru.saidgadjiev.aboutme.dao.SkillDao;
import ru.saidgadjiev.aboutme.domain.AboutMe;
import ru.saidgadjiev.aboutme.domain.Skill;

import java.sql.SQLException;
import java.util.List;

@Service
public class SkillService {

    @Autowired
    private SkillDao skillDao;

    public void create(Skill skill) throws SQLException {
        AboutMe aboutMe = new AboutMe();

        aboutMe.setId(1);
        skill.setAboutMe(aboutMe);
        skillDao.create(skill);
    }

    public int update(Integer id, Skill skill) throws SQLException {
        return skillDao.update(id, skill);
    }

    public int removeById(Integer id) throws SQLException {
        return skillDao.removeById(id);
    }
}
