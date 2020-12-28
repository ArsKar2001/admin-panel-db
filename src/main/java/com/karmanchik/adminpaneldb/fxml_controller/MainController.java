package com.karmanchik.adminpaneldb.fxml_controller;

import com.karmanchik.adminpaneldb.parser.WordInJSON;
import com.karmanchik.adminpaneldb.repository.JpaGroupRepository;
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
import org.json.JSONArray;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
@FxmlView("main-stage.fxml")
public class MainController {
    // Spring Boot инъекции
    private final JpaGroupRepository groupRepository;
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

    public MainController(JpaGroupRepository groupRepository) {
        this.groupRepository = groupRepository;
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

    public void clickExit() {
        Platform.exit();
    }

    public void importData(ActionEvent actionEvent) {
        WordInJSON wordInJSON = new WordInJSON();
        ImportData data = new ImportData(groupRepository);
        MenuItem item = ((MenuItem) actionEvent.getTarget());
        Window window = item.getParentPopup().getOwnerWindow();
        File importFile = showOpenDialogSelectFile(window);
        try {
            if (importFile != null) {
                progressBarId.setOpacity(1);
                String text = wordInJSON.wordFileAsText(importFile);
                JSONArray array = wordInJSON.textToJSON(text);
                if (item.getId().equals(importTimetableClick.getId())) {
                    data.setJsonArray(array);
                    progressBarId.progressProperty().unbind();
                    progressBarId.progressProperty().bind(data.progressProperty());

                    statusLb.textProperty().unbind();
                    statusLb.textProperty().bind(data.messageProperty());

                    data.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, workerStateEvent -> {
                        statusLb.textProperty().unbind();
                        progressBarId.prefHeightProperty().unbind();
                        progressBarId.setOpacity(0);
                        statusLb.setOpacity(0);
                    });
                    new Thread(data).start();
                }
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.WARNING, "Не удалось считать документ. Error: " + e.getMessage()).showAndWait();
        }
    }

    @FXML
    public void initialize() {
        progressBarId.setOpacity(0);
        progressBarId.setProgress(0);
        btnShowListUsers.setOnAction(this::openUserPane);
        btnShowListGroup.setOnAction(this::openGroupPane);
    }
}
