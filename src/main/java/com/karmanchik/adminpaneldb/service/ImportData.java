package com.karmanchik.adminpaneldb.service;

import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.HashMap;

@Setter
@Scope("prototype")
@NoArgsConstructor
public class ImportData implements Runnable {
    private HashMap<String, HashMap<Integer, ArrayList<String>>> timeTablesGroups;
    private LessonServiceImpl lessonService;

    @Override
    public void run() {
        this.lessonService.importOrUpdate(timeTablesGroups);
    }
}
