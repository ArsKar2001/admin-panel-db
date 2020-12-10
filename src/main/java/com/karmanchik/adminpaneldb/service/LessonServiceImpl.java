package com.karmanchik.adminpaneldb.service;


import com.karmanchik.adminpaneldb.model.Lesson;
import com.karmanchik.adminpaneldb.repository.JpaLessonRepository;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;

@Log4j
@Service
@Transactional
public class LessonServiceImpl implements LessonService {
    private final JpaLessonRepository lessonRepository;

    @Autowired
    public LessonServiceImpl(JpaLessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    @Override
    public void save(Lesson lesson) {
        this.lessonRepository.save(lesson);
    }

    @Override
    public Lesson getLesson(String group, String number, Integer day) {
        return this.lessonRepository.getLessonByGroupAndNumberAndDay(group, number, day).orElse(null);
    }

    @Override
    public Iterable<Lesson> findAll(@NotNull String group) {
        return this.lessonRepository.findAllByGroup(group);
    }

    @Override
    public List<Lesson> findAll() {
        return this.lessonRepository.findAll();
    }

    @Override
    public void delete(Lesson temp_lesson) {
        this.lessonRepository.delete(temp_lesson);
    }

    @Override
    public boolean exists(@NotNull String group, @NotNull String number, @NotNull Integer day) {
        return this.lessonRepository.existsByGroupAndNumberAndDay(group, number, day);
    }
}
