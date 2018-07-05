package ru.saidgadjiev.aboutme.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.saidgadjiev.aboutme.domain.Skill;
import ru.saidgadjiev.aboutme.service.SkillService;

import java.sql.SQLException;

@RestController
@RequestMapping("/api/skill")
public class SkillController {

    private static final Logger LOGGER = Logger.getLogger(SkillController.class);

    @Autowired
    private SkillService skillService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<Skill> createSkill(@RequestBody Skill skill) throws SQLException {
        LOGGER.debug("createSkill() " + skill);
        skillService.create(skill);

        return ResponseEntity.ok(skill);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public ResponseEntity<?> removeSkill(@RequestBody Skill skill) throws SQLException {
        LOGGER.debug("removeSkill() " + skill);
        int removed = skillService.remove(skill);

        LOGGER.debug("Skill removed " + removed);
        if (removed == 0) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseEntity<Skill> updateSkill(@RequestBody Skill skill) throws SQLException {
        LOGGER.debug("updateSkill() " + skill);

        int updated = skillService.update(skill);

        LOGGER.debug("Skill updated " + updated);
        if (updated == 0) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(skill);
    }
}
