package com.karmanchik.adminpaneldb.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.sql.Date;

@Entity
@Table(name = "notification")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Notification extends AbstractBaseEntity {
    @Column(name = "group_id")
    @NotNull
    private Integer groupId;
    @Column(name = "description")
    @NotNull
    private String description;
    @Column(name = "end_date")
    @NotNull
    private Date endDate;

    public Notification(@NotNull Integer groupId) {
        this.groupId = groupId;
    }
}
