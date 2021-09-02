package com.wz.desensitizationRuleTool.view.index;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: SystemSettingView
 * @Description: 设置页面
 * @author: Angelo
 */

@Getter
@Setter
public abstract class SystemSettingView implements Initializable {

    @FXML
    protected CheckBox exitShowAlertCheckBox;

    @FXML
    protected CheckBox addNotepadCheckBox;

    @FXML
    protected CheckBox saveStageBoundCheckBox;

    @FXML
    protected CheckBox chkNewLauncher;

    @FXML
    protected Button saveButton;

    @FXML
    protected Button cancelButton;
}
