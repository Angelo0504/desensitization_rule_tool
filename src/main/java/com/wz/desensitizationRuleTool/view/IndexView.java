package com.wz.desensitizationRuleTool.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import lombok.Getter;
import lombok.Setter;
import org.controlsfx.control.SegmentedButton;

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

    /**
     * 数据脱敏-原始数据
     * table-分页
     */
//    @FXML
//    protected Pagination pnPagination;
//
//    @FXML
//    protected Pagination pnPagination2;
//
//    @FXML
//    protected Pagination pnPagination3;

    /**
     * 没有数据-上传文件模块
     */
    @FXML
    protected AnchorPane openFileAnchorPane;

    /**
     * 数据脱敏模块
     */
    @FXML
    protected AnchorPane dataDesensitizationAnchorPane;

    /**
     * 数据还原模块
     */
    @FXML
    protected AnchorPane dataRestorationAnchorPane;

    /**
     * togglebutton 按钮组
     */
    @FXML
    protected SegmentedButton segmentedButton;

    /**
     * togglebutton 按钮组
     * 数据脱敏按钮
     */
    @FXML
    protected ToggleButton dataDesensitizationButton1;

    /**
     * togglebutton 按钮组
     * 数据还原按钮
     */
    @FXML
    protected ToggleButton dataRestorationButton1;

    @FXML
    protected TableView dataDesensitizationTable1;

    @FXML
    protected TableView dataDesensitizationTable2;

    @FXML
    protected TableView dataDesensitizationTable3;

    @FXML
    protected TableView dataDesensitizationTable4;

    @FXML
    protected ToggleButton dataDesensitizationButton11;

    @FXML
    protected ToggleButton dataDesensitizationButton12;

    @FXML
    protected ToggleButton dataDesensitizationButton13;

    @FXML
    protected TableView dataRestorationTable1;

    @FXML
    protected TableView dataRestorationTable2;

    @FXML
    protected TableView dataRestorationTable3;

    @FXML
    protected TableView dataRestorationTable4;

    @FXML
    protected ToggleButton dataRestorationButton11;

    @FXML
    protected ToggleButton dataRestorationButton12;

    @FXML
    protected ToggleButton dataRestorationButton13;

    @FXML
    protected Text text1;

    @FXML
    protected Text text2;

    @FXML
    protected Text text3;

    @FXML
    protected Text text4;

    @FXML
    protected Text text0;

    @FXML
    protected Text text5;


}
