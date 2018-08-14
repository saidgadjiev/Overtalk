package ru.saidgadjiev.aboutme.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.saidgadjiev.aboutme.dao.AboutMeDao;
import ru.saidgadjiev.aboutme.dao.SkillDao;
import ru.saidgadjiev.aboutme.domain.AboutMe;
import ru.saidgadjiev.aboutme.domain.Skill;
import ru.saidgadjiev.aboutme.model.SkillRequest;

import java.sql.SQLException;
import java.util.List;

@Service
public class SkillService {

    @Autowired
    private SkillDao skillDao;

    public Skill create(SkillRequest skillRequest) throws SQLException {
        Skill skill = new Skill();

        skill.setName(skillRequest.getName());
        skill.setPercentage(skillRequest.getPercentage());
        AboutMe aboutMe = new AboutMe();

        aboutMe.setId(1);
        skill.setAboutMe(aboutMe);

        skillDao.create(skill);

        return skill;
    }

    public int update(Integer id, SkillRequest skillRequest) throws SQLException {
        return skillDao.update(id, skillRequest.getName(), skillRequest.getPercentage());
    }

    public int removeById(Integer id) throws SQLException {
        return skillDao.removeById(id);
    }
}
