package ru.saidgadjiev.aboutme.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.saidgadjiev.aboutme.domain.Skill;
import ru.saidgadjiev.aboutme.service.SkillService;

import javax.validation.Valid;
import java.sql.SQLException;

@RestController
@RequestMapping("/api/skill")
public class SkillController {

    private static final Logger LOGGER = Logger.getLogger(SkillController.class);

    @Autowired
    private SkillService skillService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<Skill> createSkill(
            @RequestBody @Valid Skill skill,
            BindingResult bindingResult
    ) throws SQLException {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        LOGGER.debug("createSkill() " + skill);
        skillService.create(skill);

        return ResponseEntity.ok(skill);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public ResponseEntity<?> removeSkill(@PathVariable("id") Integer id) throws SQLException {
        LOGGER.debug("removeSkill() " + id);
        int removed = skillService.removeById(id);

        LOGGER.debug("Skill removed " + removed);
        if (removed == 0) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseEntity<Skill> updateSkill(
            @RequestBody @Valid Skill skill,
            BindingResult bindingResult
    ) throws SQLException {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        LOGGER.debug("updateSkill() " + skill);

        int updated = skillService.update(skill);

        LOGGER.debug("Skill updated " + updated);
        if (updated == 0) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(skill);
    }
}
