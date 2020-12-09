package com.karmanchik.adminpaneldb.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@MappedSuperclass
@Getter
@Setter
@Access(AccessType.FIELD)
public abstract class AbstractBaseEntity {

    public static final int START_SEQ = 100000;

    @Id
    @SequenceGenerator(initialValue = START_SEQ, name = "global_seq", sequenceName = "global_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global_seq")
    protected Integer id;

    public AbstractBaseEntity() {
    }
}
