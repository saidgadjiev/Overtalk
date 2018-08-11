package ru.saidgadjiev.aboutme.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.saidgadjiev.aboutme.domain.AboutMe;
import ru.saidgadjiev.aboutme.service.AboutMeService;

import java.sql.SQLException;

@RestController
@RequestMapping("/api/aboutMe")
public class AboutMeController {

    private final AboutMeService aboutMeService;

    @Autowired
    public AboutMeController(AboutMeService aboutMeService) {
        this.aboutMeService = aboutMeService;
    }

    @GetMapping(value = "")
    public ResponseEntity<AboutMe> getAboutMe() throws SQLException {
        return ResponseEntity.ok(aboutMeService.getAboutMe());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/update")
    public ResponseEntity<AboutMe> update(@RequestBody AboutMe aboutMe) throws SQLException {
        aboutMeService.update(aboutMe);

        return ResponseEntity.ok(aboutMe);
    }
}
