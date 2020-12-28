package com.karmanchik.adminpaneldb.fxml_controller;

import javafx.fxml.FXML;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractBaseController {
    @FXML
    abstract public void initialize();
}
