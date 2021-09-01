package com.wz.desensitizationRuleTool;

import com.wz.desensitizationRuleTool.fxmlView.IndexView;
import com.wz.desensitizationRuleTool.utils.StageUtils;
import com.wz.desensitizationRuleTool.utils.XJavaFxSystemUtil;
import com.xwintop.xcore.util.javafx.AlertUtil;
import com.xwintop.xcore.util.javafx.JavaFxViewUtil;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import de.felixroske.jfxsupport.GUIState;
import de.felixroske.jfxsupport.SplashScreen;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @ClassName: Main
 * @Description: 启动类
 * @author: Angelo
 */
@SpringBootApplication
@Slf4j
public class Main extends AbstractJavaFxApplicationSupport {

    public static void main(String[] args) {
        //初始化本地语言
        XJavaFxSystemUtil.initSystemLocal();
        //添加外部jar包
        XJavaFxSystemUtil.addJarByLibs();
        SplashScreen splashScreen = new SplashScreen() {
            @Override
            public String getImagePath() {
                return "/images/start.gif";
            }
        };
        launch(Main.class, IndexView.class, splashScreen, args);
//		launchApp(Main.class, IndexView.class, args);
    }

    @Override
    public void beforeInitialView(Stage stage, ConfigurableApplicationContext ctx) {
        super.beforeInitialView(stage, ctx);
        Scene scene = JavaFxViewUtil.getJFXDecoratorScene(stage, "", null, new AnchorPane());
        stage.setScene(scene);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if (AlertUtil.showConfirmAlert("确定要退出吗？")) {
                    System.exit(0);
                } else {
                    event.consume();
                }
            }
        });
        GUIState.setScene(scene);
        Platform.runLater(() -> {
            StageUtils.updateStageStyle(GUIState.getStage());
        });
    }
}
