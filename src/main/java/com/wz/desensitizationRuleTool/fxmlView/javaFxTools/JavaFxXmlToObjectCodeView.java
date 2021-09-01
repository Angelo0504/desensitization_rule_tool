package com.wz.desensitizationRuleTool.fxmlView.javaFxTools;

import de.felixroske.jfxsupport.AbstractFxmlView;
import de.felixroske.jfxsupport.FXMLView;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

@Scope("prototype")
@Lazy
@FXMLView(value = "/com/wz/desensitizationRuleTool/fxmlView/javaFxTools/JavaFxXmlToObjectCode.fxml")
public class JavaFxXmlToObjectCodeView extends AbstractFxmlView {

}
