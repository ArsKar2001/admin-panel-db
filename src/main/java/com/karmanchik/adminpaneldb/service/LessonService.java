package com.karmanchik.adminpaneldb.service;

import com.karmanchik.adminpaneldb.model.Lesson;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface LessonService {

    void save(Lesson lesson);

    Lesson getLesson(String group, String number, Integer day);

    Iterable<Lesson> findAll(@NotNull String group);
    List<Lesson> findAll();

    void delete(Lesson temp_lesson);

    boolean exists(@NotNull String group, @NotNull String number, @NotNull Integer day);
}
