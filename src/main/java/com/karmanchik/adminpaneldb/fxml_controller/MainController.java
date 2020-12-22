package com.karmanchik.adminpaneldb.fxml_controller;

import com.karmanchik.adminpaneldb.model.Lesson;
import com.karmanchik.adminpaneldb.parser.ParserDocx;
import com.karmanchik.adminpaneldb.service.GroupService;
import com.karmanchik.adminpaneldb.service.LessonService;
import com.karmanchik.adminpaneldb.task.ImportData;
import javafx.application.Platform;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;

@Component
@FxmlView("main-stage.fxml")
public class MainController {
    // Spring Boot инъекции
    private final LessonService lessonService;
    private final GroupService groupService;
    // JavaFX инъекции
    @FXML
    public Button btnShowListUsers;
    @FXML
    public Button btnShowListGroup;
    @FXML
    public Label statusLb;
    @FXML
    public ProgressBar progressBarId;
    @FXML
    public MenuItem importTimetableClick;
    @FXML
    public MenuItem importReplacementClick;
    @FXML
    public AnchorPane groupPane;
    @FXML
    public AnchorPane userPane;
    @FXML
    public ListView<Lesson> listGroup;

    public MainController(LessonService lessonService, GroupService groupService) {
        this.lessonService = lessonService;
        this.groupService = groupService;
    }

    private void openGroupPane(ActionEvent actionEvent) {
        userPane.setOpacity(0);
        groupPane.setOpacity(1);
    }

    private void openUserPane(ActionEvent actionEvent) {
        userPane.setOpacity(1);
        groupPane.setOpacity(0);
    }

    public File showOpenDialogSelectFile(Window window) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.setInitialFileName("src/main/resources/files");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Docx Files", "*.docx"));
        return fileChooser.showOpenDialog(window);
    }

    public void clickExit(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void importData(ActionEvent actionEvent) {
        ImportData data = new ImportData(lessonService, groupService);
        MenuItem item = ((MenuItem) actionEvent.getTarget());
        Window window = item.getParentPopup().getOwnerWindow();
        File importFile = showOpenDialogSelectFile(window);

        if(importFile != null) {
            ParserDocx parserDocx = new ParserDocx(importFile);
            try {
                if(item.getId().equals(importTimetableClick.getId())) {
                    parserDocx.parseTimetable();
                    data.setTimeTablesGroups(parserDocx.getTimeTablesGroups());

                    progressBarId.progressProperty().unbind();
                    progressBarId.progressProperty().bind(data.progressProperty());

                    statusLb.textProperty().unbind();
                    statusLb.textProperty().bind(data.messageProperty());

                    data.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, workerStateEvent -> {
                        statusLb.textProperty().unbind();
                        progressBarId.prefHeightProperty().unbind();
                    });
                    new Thread(data).start();
                } else if(item.getId().equals(importReplacementClick.getId())) {
                    parserDocx.parserReplacement();

                }
            } catch (Exception e) {
                new Alert(Alert.AlertType.WARNING, "Не удалось считать документ. Error: " + e.getMessage()).showAndWait();
            }
        }
    }

    @FXML
    public void initialize() {
        progressBarId.setProgress(0);
        btnShowListUsers.setOnAction(this::openUserPane);
        btnShowListGroup.setOnAction(this::openGroupPane);
    }
}
