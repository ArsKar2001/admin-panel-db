package com.karmanchik.adminpaneldb.service;


import com.karmanchik.adminpaneldb.model.Lesson;
import com.karmanchik.adminpaneldb.repository.JpaLessonRepository;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
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

    public void importOrUpdate(HashMap<String, HashMap<Integer, ArrayList<String>>> hashMap) {
        getListLessons(hashMap).forEach(lesson -> {
            final Lesson temp_lesson
                    = lessonRepository.getLessonByGroupAndNumberAndDay(lesson.getGroup(), lesson.getNumber(), lesson.getDay()).orElse(null);
            if(temp_lesson == null) {
                lessonRepository.save(lesson);
            } else {
                lessonRepository.delete(temp_lesson);
                lessonRepository.save(lesson);
            }
        });
    }

    private List<Lesson> getListLessons(HashMap<String, HashMap<Integer, ArrayList<String>>> hashMap) {
        List<Lesson> lessons = new ArrayList<>();
        hashMap.forEach((group, integerArrayListHashMap) ->
                integerArrayListHashMap.forEach((day, strings) ->
                        strings.forEach(s -> {
                            String[] split = s.split(";");
                            if(split.length == 1) {
                                lessons.add(new Lesson(group, day, split[0]));
                            } else if(split.length == 3 ) {
                                lessons.add(new Lesson(group, day, split[0], "-", split[2], split[1]));
                            } else {
                                lessons.add(new Lesson(group, day, split[0], split[3], split[2], split[1]));
                            }
                        })
                )
        );
        log.debug("Create list: "+lessons.toString());
        return lessons;
    }

    @Transient
    public boolean isNew(Lesson lesson) {
        return this.lessonRepository.existsByDay(lesson.getDay()) &&
                this.lessonRepository.existsByGroup(lesson.getGroup()) &&
                this.lessonRepository.existsByNumber(lesson.getNumber());
    }

    @Override
    public Lesson save(Lesson lesson) {
        return this.lessonRepository.save(lesson);
    }
}
