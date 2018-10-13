package ru.saidgadjiev.aboutme.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.saidgadjiev.aboutme.dao.AboutMeDao;
import ru.saidgadjiev.aboutme.domain.Aboutme;
import ru.saidgadjiev.aboutme.model.AboutMeDetails;

import java.sql.SQLException;

@Service
public class AboutMeService {

    @Autowired
    private AboutMeDao aboutmeDao;

    public Aboutme getAboutMe() throws SQLException {
        return aboutmeDao.getAboutMe();
    }

    public Aboutme update(AboutMeDetails aboutmeDetails) throws SQLException {
        Aboutme aboutme = aboutmeDao.getAboutMe();

        aboutme.setPost(aboutmeDetails.getPost());
        aboutme.setPlaceOfResidence(aboutmeDetails.getPlaceOfResidence());

        aboutmeDao.update(aboutme);

        return aboutme;
    }
}
