package ru.saidgadjiev.aboutme.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.saidgadjiev.aboutme.domain.Skill;
import ru.saidgadjiev.aboutme.json.SkillJsonBuilder;
import ru.saidgadjiev.aboutme.model.SkillDetails;
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
    public ResponseEntity<ObjectNode> create(
            @RequestBody @Valid SkillDetails request,
            BindingResult bindingResult
    ) throws SQLException {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        Skill skill = skillService.create(request);

        return ResponseEntity.ok(new SkillJsonBuilder()
                .id(skill.getId())
                .name(skill.getName())
                .percentage(skill.getPercentage())
                .build());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) throws SQLException {
        int removed = skillService.removeById(id);

        if (removed == 0) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping(value = "/update/{id}")
    public ResponseEntity<ObjectNode> update(
            @PathVariable("id") Integer id,
            @RequestBody @Valid SkillDetails request,
            BindingResult bindingResult
    ) throws SQLException {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        Skill updated = skillService.update(id, request);

        if (updated == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(new SkillJsonBuilder()
                .name(updated.getName())
                .percentage(updated.getPercentage())
                .build());
    }
}
