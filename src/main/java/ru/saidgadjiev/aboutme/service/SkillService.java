package ru.saidgadjiev.aboutme.service;

import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.saidgadjiev.aboutme.dao.AboutMeDao;
import ru.saidgadjiev.aboutme.dao.SkillDao;
import ru.saidgadjiev.aboutme.domain.AboutMe;
import ru.saidgadjiev.aboutme.domain.Skill;
import ru.saidgadjiev.aboutme.model.SkillDetails;

import java.sql.SQLException;

@Service
public class SkillService {

    @Autowired
    private AboutMeDao aboutMe;

    @Autowired
    private SkillDao skillDao;

    public Skill create(SkillDetails skillDetails) throws SQLException {
        Skill skill = new Skill();

        skill.setName(skillDetails.getName());
        skill.setPercentage(skillDetails.getPercentage());
        skill.setAboutMe(aboutMe.getAboutMe());

        skillDao.create(skill);

        return skill;
    }

    @Nullable
    public Skill update(Integer id, SkillDetails skillDetails) throws SQLException {
        Skill skill = skillDao.getById(id);

        if (skill == null) {
            return null;
        }

        skill.setName(skillDetails.getName());
        skill.setPercentage(skillDetails.getPercentage());

        skillDao.update(skill);

        return skill;
    }

    public int removeById(Integer id) throws SQLException {
        return skillDao.removeById(id);
    }
}
