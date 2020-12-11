package com.karmanchik.adminpaneldb.service;

import com.karmanchik.adminpaneldb.model.Lesson;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface LessonService {

    void save(Lesson lesson);

    Lesson getLesson(@NotNull Integer groupId, @NotNull String number, @NotNull Integer day);

    List<Lesson> findAll(@NotNull Integer groupId);
    List<Lesson> findAll();

    void delete(Lesson temp_lesson);

    boolean exists(@NotNull Integer groupId, @NotNull String number, @NotNull Integer day);
}
