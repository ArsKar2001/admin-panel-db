package com.karmanchik.adminpaneldb.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NamedNativeQueries(
        value = {
                @NamedNativeQuery(
                        name = "Lesson.get_json_fields_from_text",
                        query = "select * from get_json_fields_from_text()"
                )
        }
)
@Entity
public class Lesson extends AbstractBaseEntity {
    @NotNull
    private String groupName;
}
