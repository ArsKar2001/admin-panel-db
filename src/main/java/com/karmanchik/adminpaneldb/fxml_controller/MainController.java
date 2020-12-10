package com.karmanchik.adminpaneldb.fxml_controller;

import com.karmanchik.adminpaneldb.model.Lesson;
import com.karmanchik.adminpaneldb.parser.ParserDocx;
import com.karmanchik.adminpaneldb.service.LessonService;
import com.karmanchik.adminpaneldb.task.ImportData;
import javafx.application.Platform;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
@FxmlView("main-stage.fxml")
public class MainController {
    // Spring Boot инъекции
    private final LessonService lessonService;
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
    public MenuItem importClick;

    public MainController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @FXML
    public void initialize() {
        progressBarId.setProgress(0);
        importClick.setOnAction(this::clickImport);
    }



    private void clickImport(ActionEvent actionEvent) {
        Window window = ((MenuItem) actionEvent.getTarget()).getParentPopup().getOwnerWindow();
        File importFile = showOpenDialogSelectFile(window);
        if(importFile != null) {
            ParserDocx parserDocx = new ParserDocx(importFile);
            parserDocx.parse();

            ImportData data = new ImportData(lessonService);
            data.setTimeTablesGroups(parserDocx.getTimeTablesGroups());

            progressBarId.progressProperty().unbind();
            progressBarId.progressProperty().bind(data.progressProperty());

            statusLb.textProperty().unbind();
            statusLb.textProperty().bind(data.messageProperty());

            data.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, workerStateEvent -> {
                List<Lesson> lessons = data.getValue();
                statusLb.textProperty().unbind();
                statusLb.setText("Importing: "+lessons.size());
            });

            new Thread(data).start();
        }
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
}
