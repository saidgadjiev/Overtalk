package ru.saidgadjiev.aboutme.json;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.RawValue;
import ru.saidgadjiev.aboutme.domain.Skill;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by said on 14.08.2018.
 */
public class AboutMeJsonBuilder {

    private static final String DATE_FORMAT = "dd-MM-yyyy";

    private final JsonBuilder jsonBuilder = new JsonBuilder();

    public AboutMeJsonBuilder() {
        jsonBuilder.addDateFormat(DATE_FORMAT);
    }

    public AboutMeJsonBuilder id(int id) {
        jsonBuilder.add("id", id);

        return this;
    }

    public AboutMeJsonBuilder fio(String fio) {
        jsonBuilder.add("fio", fio);

        return this;
    }

    public AboutMeJsonBuilder dateOfBirth(LocalDate dateOfBirth) {
        jsonBuilder.add("dateOfBirth", dateOfBirth, DATE_FORMAT);

        return this;
    }

    public AboutMeJsonBuilder placeOfResidence(String placeOfResidence) {
        jsonBuilder.add("placeOfResidence", placeOfResidence);

        return this;
    }

    public AboutMeJsonBuilder educationLevel(String educationLevel) {
        jsonBuilder.add("educationLevel", educationLevel);

        return this;
    }

    public AboutMeJsonBuilder post(String post) {
        jsonBuilder.add("post", post);

        return this;
    }

    public AboutMeJsonBuilder additionalInformation(String additionalInformation) {
        jsonBuilder.add("additionalInformation", additionalInformation);

        return this;
    }

    public AboutMeJsonBuilder skills(List<Skill> skills) {
        List<RawValue> jsonSkills = new ArrayList<>();

        for (Skill skill: skills) {
            RawValue rawValue = new RawValue(new SkillJsonBuilder()
                    .id(skill.getId())
                    .name(skill.getName())
                    .percentage(skill.getPercentage())
                    .build());

            jsonSkills.add(rawValue);
        }

        jsonBuilder.add("skills", jsonSkills);

        return this;
    }

    public ObjectNode build() {
        return jsonBuilder.build();
    }
}
