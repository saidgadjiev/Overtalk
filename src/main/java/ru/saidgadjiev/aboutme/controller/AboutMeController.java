package ru.saidgadjiev.aboutme.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.saidgadjiev.aboutme.domain.AboutMe;
import ru.saidgadjiev.aboutme.service.AboutMeService;

import java.sql.SQLException;

@RestController
@RequestMapping("/api/aboutme")
public class AboutMeController {

    private static final Logger LOGGER = Logger.getLogger(AboutMeController.class);

    @Autowired
    private AboutMeService aboutMeService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    private ResponseEntity<AboutMe> getAboutMe() throws SQLException {
        return ResponseEntity.ok(aboutMeService.getAboutMe());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    private ResponseEntity<AboutMe> update(@RequestBody AboutMe aboutMe) throws SQLException {
        int updated = aboutMeService.update(aboutMe);

        LOGGER.debug("Biography updated " + updated);

        if (updated == 0) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(aboutMe);
    }
}
