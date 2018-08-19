package ru.saidgadjiev.aboutme.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.saidgadjiev.aboutme.dao.AboutMeDao;
import ru.saidgadjiev.aboutme.domain.AboutMe;
import ru.saidgadjiev.aboutme.model.AboutMeDetails;

import java.sql.SQLException;

@Service
public class AboutMeService {

    @Autowired
    private AboutMeDao aboutMeDao;

    public AboutMe getAboutMe() throws SQLException {
        return aboutMeDao.getAboutMe();
    }

    public AboutMe update(AboutMeDetails aboutMeDetails) throws SQLException {
        AboutMe aboutMe = aboutMeDao.getAboutMe();

        aboutMe.setPost(aboutMeDetails.getPost());
        aboutMe.setPlaceOfResidence(aboutMeDetails.getPlaceOfResidence());

        aboutMeDao.update(aboutMe);

        return aboutMe;
    }
}
