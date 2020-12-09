package com.karmanchik.adminpaneldb;

import com.karmanchik.adminpaneldb.fxml_controller.MainController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@Log4j
public class JavaFxApplication extends Application {

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void start(Stage stage) {
        FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);
        Parent parent = fxWeaver.loadView(MainController.class);
        stage.setScene(new Scene(parent));
        stage.setTitle("Панель управления");
        stage.getIcons().add(new Image("/img/logo.png"));
        stage.show();
    }

    @Override
    public void init() {
        String[] args = getParameters().getRaw().toArray(new String[0]);
        this.applicationContext = new SpringApplicationBuilder()
                .sources(AdminPanelDbApplication.class)
                .run(args);
    }

    @Override
    public void stop() {
        this.applicationContext.close();
        Platform.exit();
    }
}
