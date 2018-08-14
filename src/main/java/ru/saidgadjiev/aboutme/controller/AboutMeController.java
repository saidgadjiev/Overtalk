package ru.saidgadjiev.aboutme.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.saidgadjiev.aboutme.domain.AboutMe;
import ru.saidgadjiev.aboutme.json.AboutMeJsonBuilder;
import ru.saidgadjiev.aboutme.model.AboutMeRequest;
import ru.saidgadjiev.aboutme.service.AboutMeService;

import javax.validation.Valid;
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
    public ResponseEntity<ObjectNode> getAboutMe() throws SQLException {
        AboutMe aboutMe = aboutMeService.getAboutMe();

        ObjectNode response = new AboutMeJsonBuilder()
                .id(aboutMe.getId())
                .fio(aboutMe.getFio())
                .dateOfBirth(aboutMe.getDateOfBirth())
                .placeOfResidence(aboutMe.getPlaceOfResidence())
                .educationLevel(aboutMe.getEducationLevel())
                .post(aboutMe.getPost())
                .additionalInformation(aboutMe.getAdditionalInformation())
                .skills(aboutMe.getSkills())
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/update")
    public ResponseEntity<ObjectNode> update(
            @RequestBody @Valid AboutMeRequest aboutMeRequest,
            BindingResult bindingResult
    ) throws SQLException {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        aboutMeService.update(aboutMeRequest);

        ObjectNode response = new AboutMeJsonBuilder()
                .post(aboutMeRequest.getPost())
                .placeOfResidence(aboutMeRequest.getPlaceOfResidence())
                .build();

        return ResponseEntity.ok(response);
    }
}
