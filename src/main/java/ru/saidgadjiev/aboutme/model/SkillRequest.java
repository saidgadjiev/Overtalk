package ru.saidgadjiev.aboutme.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by said on 04.03.2018.
 */
public class SkillRequest {

    @NotNull
    @Size(min = 1)
    private String name;

    @NotNull
    @Min(0)
    private int percentage;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    @Override
    public String toString() {
        return "SkillRequest{" +
                "name='" + name + '\'' +
                ", percentage='" + percentage + '\'' +
                '}';
    }
}
