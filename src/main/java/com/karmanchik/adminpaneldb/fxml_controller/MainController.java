package com.karmanchik.adminpaneldb.fxml_controller;

import com.karmanchik.adminpaneldb.parser.ParserDocx;
import com.karmanchik.adminpaneldb.service.ImportData;
import com.karmanchik.adminpaneldb.service.LessonServiceImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.FileChooser;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@FxmlView("main.fxml")
public class MainController {

    private final LessonServiceImpl lessonServiceImpl;

    public MainController(LessonServiceImpl lessonServiceImpl) {
        this.lessonServiceImpl = lessonServiceImpl;
    }

    @FXML
    public void initialize() {

    }

    @FXML
    public void importClick(ActionEvent actionEvent) {
        File importFile = showOpenDialogSelectFile((Node) actionEvent.getSource());
        if(importFile != null) {
            ParserDocx parserDocx = new ParserDocx(importFile);
            parserDocx.parse();

            ImportData data = new ImportData();
            data.setLessonService(lessonServiceImpl);
            data.setTimeTablesGroups(parserDocx.getTimeTablesGroups());

            Thread thread = new Thread(data);
            thread.setName("Import from file: "+importFile.getName());
            thread.start();
        }
    }

    public File showOpenDialogSelectFile(Node node) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.setInitialFileName("src/main/resources/files");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Docx Files", "*.docx"));
        return fileChooser.showOpenDialog(node.getScene().getWindow());
    }
}
