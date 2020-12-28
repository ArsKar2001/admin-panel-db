package com.karmanchik.adminpaneldb.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "schedule", uniqueConstraints = {@UniqueConstraint(columnNames = "group_name", name = "groups_group_name_uindex")})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Group extends AbstractBaseEntity {
    @Column(name = "group_name", nullable = false, unique = true)
    @NotNull
    private String groupName;
    @Column(name = "timetable")
    @NotNull
    private String timetable;

    public Group(@NotNull String groupName) {
        this.groupName = groupName;
    }
}
