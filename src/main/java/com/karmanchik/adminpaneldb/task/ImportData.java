package com.karmanchik.adminpaneldb.task;

import com.karmanchik.adminpaneldb.model.Lesson;
import com.karmanchik.adminpaneldb.service.LessonService;
import javafx.concurrent.Task;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Log4j
@Setter
@Scope("prototype")
@Component
public class ImportData extends Task<List<Lesson>> {
    private HashMap<String, HashMap<Integer, ArrayList<String>>> timeTablesGroups;
    private LessonService lessonService;
    private List<Lesson> allLesson;

    public ImportData(LessonService lessonService) {
        this.lessonService = lessonService;
        this.allLesson = this.lessonService.findAll();
    }

    @Override
    protected List<Lesson> call() {
        log.debug("Запустили задачу "+toString());
        List<Lesson> importLessons = this.getListLessons(timeTablesGroups);
        List<Lesson> tempLessonList = new ArrayList<>();
        int count = importLessons.size();
        int i = 0;

        for (Lesson importLesson : importLessons) {
            if(!isEmptyLesson(importLesson)) {
                this.lessonService.save(importLesson);
            } else if (isUpdateLesson(importLesson)) {
                final Lesson lesson = this.lessonService.getLesson(importLesson.getGroup(), importLesson.getNumber(), importLesson.getDay());
                this.lessonService.delete(lesson);
                this.lessonService.save(importLesson);
            }
            i++;
            this.updateProgress(i, count);
            this.updateMessage("Importing to database: "+i+"/"+count);
            tempLessonList.add(importLesson);
        }
        return tempLessonList;
    }

    private boolean isUpdateLesson(Lesson importLesson) {
        return this.lessonService.exists(importLesson.getGroup(), importLesson.getNumber(), importLesson.getDay());
    }

    private boolean isEmptyLesson(Lesson lesson) {
        List<Lesson> allLesson = this.allLesson;
        for (Lesson tempLesson : allLesson) {
            return tempLesson.getGroup().equals(lesson.getGroup()) &&
                    tempLesson.getDay().equals(lesson.getDay()) &&
                    tempLesson.getNumber().equals(lesson.getNumber()) &&
                    tempLesson.getAudience().equals(lesson.getAudience()) &&
                    tempLesson.getDiscipline().equals(lesson.getDiscipline()) &&
                    tempLesson.getTeacher().equals(lesson.getTeacher());
        }
        return false;
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
        log.debug("Create list: "+lessons.toArray().toString());
        return lessons;
    }
}
