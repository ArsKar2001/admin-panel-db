package com.karmanchik.adminpaneldb.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "lessons")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Lesson extends AbstractBaseEntity {
    @Column(name = "group_name")
    @NotNull
    private String group;

    @Column(name = "day")
    @NotNull
    private Integer day;

    @Column(name = "number")
    @NotNull
    private String number;

    @Column(name = "teacher")
    @NotNull
    private String teacher;

    @Column(name = "audience")
    @NotNull
    private String audience;

    @Column(name = "discipline")
    @NotNull
    private String discipline;

    public Lesson(@NotNull String group, @NotNull Integer day, @NotNull String number) {
        this.group = group;
        this.day = day;
        this.number = number;
        this.audience = "-";
        this.discipline = "-";
        this.teacher = "-";
    }


}
