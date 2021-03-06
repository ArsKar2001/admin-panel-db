package com.karmanchik.adminpaneldb.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import com.vladmihalcea.hibernate.type.json.*;


import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "groups", uniqueConstraints = {@UniqueConstraint(columnNames = "group_name", name = "groups_group_name_uindex")})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class Group extends AbstractBaseEntity {
    @Column(name = "group_name", nullable = false, unique = true)
    @NotNull
    private String groupName;
    @Column(name = "timetable")
    @NotNull
    @Type(type = "json")
    private String timetable;

    public Group(@NotNull String groupName) {
        this.groupName = groupName;
    }
}
