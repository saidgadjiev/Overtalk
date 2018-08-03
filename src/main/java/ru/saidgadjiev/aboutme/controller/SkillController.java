package ru.saidgadjiev.aboutme.controller;

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

    @Autowired
    private SkillService skillService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/create")
    public ResponseEntity<Skill> createSkill(
            @RequestBody @Valid Skill skill,
            BindingResult bindingResult
    ) throws SQLException {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        skillService.create(skill);

        return ResponseEntity.ok(skill);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/delete/{id}")
    public ResponseEntity<?> removeSkill(@PathVariable("id") Integer id) throws SQLException {
        int removed = skillService.removeById(id);

        if (removed == 0) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/update")
    public ResponseEntity<Skill> updateSkill(
            @RequestBody @Valid Skill skill,
            BindingResult bindingResult
    ) throws SQLException {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        int updated = skillService.update(skill);

        if (updated == 0) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(skill);
    }
}
