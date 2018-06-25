package ru.saidgadjiev.aboutme.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.saidgadjiev.aboutme.domain.AboutMe;
import ru.saidgadjiev.aboutme.domain.Skill;
import ru.saidgadjiev.aboutme.model.ResponseMessage;
import ru.saidgadjiev.aboutme.service.AboutMeService;

import java.sql.SQLException;

@RestController
@RequestMapping("/api/aboutme")
public class AboutMeController {

    private static final Logger LOGGER = Logger.getLogger(AboutMeController.class);

    @Autowired
    private AboutMeService aboutMeService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    private ResponseEntity<ResponseMessage<AboutMe>> getAboutMe() throws SQLException {
        return ResponseEntity.ok(new ResponseMessage<>("", aboutMeService.getAboutMe()));
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    private ResponseEntity<ResponseMessage<String>> updateBiography(@RequestBody String biography) throws SQLException {
        int updated = aboutMeService.updateBiography(biography);

        LOGGER.debug("Biography updated " + updated);

        if (updated == 0) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(new ResponseMessage<>("", biography));
    }
}
