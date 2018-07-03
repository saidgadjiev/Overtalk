package ru.saidgadjiev.aboutme.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.saidgadjiev.aboutme.dao.AboutMeDao;
import ru.saidgadjiev.aboutme.domain.AboutMe;

import java.sql.SQLException;

@Service
public class AboutMeService {

    @Autowired
    private AboutMeDao aboutMeDao;

    public AboutMe getAboutMe() throws SQLException {
        return aboutMeDao.getAboutMe();
    }

    public int update(AboutMe aboutMe) throws SQLException {
        return aboutMeDao.update(aboutMe);
    }
}
