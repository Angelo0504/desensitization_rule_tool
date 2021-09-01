package com.wz.desensitizationRuleTool.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.web.WebView;
import lombok.Getter;
import lombok.Setter;

import javax.swing.text.html.ImageView;
import java.util.Map;
import java.util.ResourceBundle;

@Getter
@Setter
public abstract class IndexView implements Initializable {
    protected ResourceBundle bundle;

    @FXML
    protected MenuBar mainMenuBar;

    @FXML
    protected Menu toolsMenu;

    @FXML
    protected Menu netWorkToolsMenu;

    @FXML
    protected Menu helpMenu;

    @FXML
    protected Menu fileToolMenu;

    /**
     * 数据脱敏-原始数据
     * table
     */
    @FXML
    protected TableView stmtYsDataTable;


}
