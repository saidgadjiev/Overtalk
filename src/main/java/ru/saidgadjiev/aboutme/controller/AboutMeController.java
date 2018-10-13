package ru.saidgadjiev.aboutme.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.saidgadjiev.aboutme.domain.Aboutme;
import ru.saidgadjiev.aboutme.json.AboutMeJsonBuilder;
import ru.saidgadjiev.aboutme.model.AboutMeDetails;
import ru.saidgadjiev.aboutme.service.AboutMeService;

import javax.validation.Valid;
import java.sql.SQLException;

@RestController
@RequestMapping("/api/aboutme")
public class AboutMeController {

    private final AboutMeService aboutmeService;

    @Autowired
    public AboutMeController(AboutMeService aboutmeService) {
        this.aboutmeService = aboutmeService;
    }

    @GetMapping(value = "")
    public ResponseEntity<ObjectNode> getAboutMe() throws SQLException {
        Aboutme aboutme = aboutmeService.getAboutMe();

        ObjectNode response = new AboutMeJsonBuilder()
                .id(aboutme.getId())
                .fio(aboutme.getFio())
                .dateOfBirth(aboutme.getDateOfBirth())
                .placeOfResidence(aboutme.getPlaceOfResidence())
                .educationLevel(aboutme.getEducationLevel())
                .post(aboutme.getPost())
                .additionalInformation(aboutme.getAdditionalInformation())
                .skills(aboutme.getSkills())
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping(value = "/update")
    public ResponseEntity<ObjectNode> update(
            @RequestBody @Valid AboutMeDetails aboutmeDetails,
            BindingResult bindingResult
    ) throws SQLException {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        Aboutme aboutme = aboutmeService.update(aboutmeDetails);

        ObjectNode response = new AboutMeJsonBuilder()
                .post(aboutme.getPost())
                .placeOfResidence(aboutme.getPlaceOfResidence())
                .build();

        return ResponseEntity.ok(response);
    }
}
