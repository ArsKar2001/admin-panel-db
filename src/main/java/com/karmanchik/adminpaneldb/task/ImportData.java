package com.karmanchik.adminpaneldb.task;

import com.karmanchik.adminpaneldb.model.Group;
import com.karmanchik.adminpaneldb.model.Lesson;
import com.karmanchik.adminpaneldb.service.GroupService;
import com.karmanchik.adminpaneldb.service.LessonService;
import javafx.concurrent.Task;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Log4j
@Scope("prototype")
@Component
public class ImportData extends Task<List<Lesson>> {
    private HashMap<String, HashMap<Integer, ArrayList<String>>> timeTablesGroups;

    private final LessonService lessonService;
    private final GroupService groupService;

    private final List<Lesson> allLessons;
    private final List<Group> allGroups;


    public ImportData(LessonService lessonService, GroupService groupService) {
        this.lessonService = lessonService;
        this.groupService = groupService;
        this.allGroups = this.groupService.findAll();
        this.allLessons = this.lessonService.findAll();
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
                final Lesson lesson = this.lessonService.getLesson(importLesson.getGroupId(), importLesson.getNumber(), importLesson.getDay());
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
        return this.lessonService.exists(importLesson.getGroupId(), importLesson.getNumber(), importLesson.getDay());
    }

    private boolean isEmptyLesson(Lesson lesson) {
        for (Lesson tempLesson : this.allLessons) {
            return tempLesson.getGroupId().equals(lesson.getGroupId()) &&
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
        hashMap.forEach((group, integerArrayListHashMap) -> {
            int groupId = getGroupId(group);
            integerArrayListHashMap.forEach((day, strings) ->
                    strings.forEach(s -> {
                        String[] split = s.split(";");
                        if(split.length == 1) {
                            lessons.add(new Lesson(groupId, day, split[0]));
                        } else if(split.length == 3 ) {
                            lessons.add(new Lesson(groupId, day, split[0], "-", split[2], split[1]));
                        } else {
                            lessons.add(new Lesson(groupId, day, split[0], split[3], split[2], split[1]));
                        }
                    })
            );
        });
        log.debug("Create list: "+ Arrays.toString(lessons.toArray()));
        return lessons;
    }

    private int getGroupId(String group_name) {
        if(this.allGroups.size() == 0)
            return getNewGroup(group_name).getId();
        for (Group group : this.allGroups) {
            if(group.getGroupName().equals(group_name)) {
                return group.getId();
            } else {
                return getNewGroup(group_name).getId();
            }
        }
        return 0;
    }

    private Group getNewGroup(String group) {
        Group newGroup = this.groupService.save(new Group(group));
        this.allGroups.add(newGroup);
        return newGroup;
    }

    public void setTimeTablesGroups(HashMap<String, HashMap<Integer, ArrayList<String>>> timeTablesGroups) {
        this.timeTablesGroups = timeTablesGroups;
    }
}
