package com.wz.desensitizationRuleTool.services;

import com.wz.desensitizationRuleTool.controller.IndexController;
import com.wz.desensitizationRuleTool.common.logback.ConsoleLogAppender;
import com.wz.desensitizationRuleTool.model.PluginJarInfo;
import com.wz.desensitizationRuleTool.plugin.PluginLoader;
import com.wz.desensitizationRuleTool.utils.Config;
import com.xwintop.xcore.javafx.dialog.FxAlerts;
import com.xwintop.xcore.util.javafx.JavaFxViewUtil;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

@Setter
public class IndexService {
    private IndexController indexController;

    public IndexService(IndexController indexController) {
        this.indexController = indexController;
    }

    public void setLanguageAction(String languageType) throws Exception {
        if ("简体中文".equals(languageType)) {
            Config.set(Config.Keys.Locale, Locale.SIMPLIFIED_CHINESE);
        } else if ("English".equals(languageType)) {
            Config.set(Config.Keys.Locale, Locale.US);
        }
        FxAlerts.info("", indexController.getBundle().getString("SetLanguageText"));
    }

    public ContextMenu getSelectContextMenu(String selectText) {
        selectText = selectText.toLowerCase();
        ContextMenu contextMenu = new ContextMenu();
        for (MenuItem menuItem : indexController.getMenuItemMap().values()) {
            if (menuItem.getText().toLowerCase().contains(selectText)) {
                MenuItem menu_tab = new MenuItem(menuItem.getText(), menuItem.getGraphic());
                menu_tab.setOnAction(event1 -> {
                    menuItem.fire();
                });
                contextMenu.getItems().add(menu_tab);
            }
        }
        return contextMenu;
    }

    /**
     * //TODO 注释掉
     * @Title: addContent
     * @Description: 添加Content内容
     */
    public void addContent(String title, String url, String resourceBundleName, String iconPath) {

        PluginJarInfo plugin = new PluginJarInfo();
        plugin.setTitle(title);
        plugin.setFxmlPath(url);
        plugin.setBundleName(resourceBundleName);
        plugin.setIconPath(iconPath);
    }
}
