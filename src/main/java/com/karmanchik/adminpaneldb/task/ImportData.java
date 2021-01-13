package com.karmanchik.adminpaneldb.task;

import com.karmanchik.adminpaneldb.model.Group;
import com.karmanchik.adminpaneldb.repository.JpaGroupRepository;
import javafx.concurrent.Task;
import lombok.extern.log4j.Log4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Log4j
@Scope("prototype")
@Component
public class ImportData extends Task<Integer> {

    private JSONArray jsonArray;
    private final JpaGroupRepository groupRepository;

    public ImportData(JpaGroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    protected Integer call() {
        if (jsonArray != null) {
            int i = 0;
            for (Object o : jsonArray) {
                JSONObject object = (JSONObject) o;
                String group_name = object.getString("group_name");
                JSONArray data = object.getJSONArray("timetable");
                JSONObject timetable = new JSONObject();
                timetable.put("data", data);

                Group group = groupRepository.getByGroupName(group_name)
                        .orElseGet(() -> groupRepository.save(new Group(group_name)));
                group.setTimetable(timetable.toString());
                groupRepository.save(group);
                i++;
                updateMessage("Importing to database: "+i+"/"+jsonArray.length());
                updateProgress(i, jsonArray.length());
            }
            return i;
        } else {
            throw new RuntimeException();
        }
    }
    public void setJsonArray(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }
}
