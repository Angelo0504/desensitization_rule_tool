package com.wz.desensitizationRuleTool.controller;

import com.wz.desensitizationRuleTool.controller.index.PluginManageController;
import com.wz.desensitizationRuleTool.model.ToolFxmlLoaderConfiguration;
import com.wz.desensitizationRuleTool.services.IndexService;
import com.wz.desensitizationRuleTool.services.index.PluginManageService;
import com.wz.desensitizationRuleTool.services.index.SystemSettingService;
import com.wz.desensitizationRuleTool.utils.Config;
import com.wz.desensitizationRuleTool.utils.PoiExcelUtil;
import com.wz.desensitizationRuleTool.utils.SpringUtil;
import com.wz.desensitizationRuleTool.utils.XJavaFxSystemUtil;
import com.wz.desensitizationRuleTool.view.IndexView;
import com.xwintop.xcore.util.ConfigureUtil;
import com.xwintop.xcore.util.HttpClientUtil;
import com.xwintop.xcore.util.javafx.AlertUtil;
import com.xwintop.xcore.util.javafx.JavaFxSystemUtil;
import com.xwintop.xcore.util.javafx.JavaFxViewUtil;
import de.felixroske.jfxsupport.AbstractFxmlView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static com.wz.desensitizationRuleTool.utils.Config.Keys.NotepadEnabled;
import static com.xwintop.xcore.util.javafx.JavaFxViewUtil.setControllerOnCloseRequest;

/**
 * @ClassName: IndexController
 * @Description: 主页
 * @author: Angelo
 * @date: 2017年7月20日 下午1:50:00
 */
@FXMLController
@Slf4j
@Getter
@Setter
public class IndexController extends IndexView {
    public static final String QQ_URL = "https://support.qq.com/product/127577";
    public static final String STATISTICS_URL = "https://xwintop.gitee.io/maven/tongji/xJavaFxTool.html";

    private Map<String, Menu> menuMap = new HashMap<String, Menu>();
    private Map<String, MenuItem> menuItemMap = new HashMap<String, MenuItem>();
    private IndexService indexService = new IndexService(this);
    private ContextMenu contextMenu = new ContextMenu();
    private ObservableList<Map<String, Object>> analysisResults = FXCollections.observableArrayList();
    private ObservableList<Map> dataMap = FXCollections.observableArrayList();
    private Map<String, String> headMap = new HashMap<String, String>();

    /**
     * 初始化
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.bundle = resources;
        initView();
        initEvent();
        initService();
    }
    private void initView() {
        menuMap.put("toolsMenu", toolsMenu);
        File libPath = new File("libs/");
        // 获取所有的.jar和.zip文件
        File[] jarFiles = libPath.listFiles((dir, name) -> name.endsWith(".jar"));
        if (jarFiles != null) {
            for (File jarFile : jarFiles) {
                if (!PluginManageService.isPluginEnabled(jarFile.getName())) {
                    continue;
                }
                try {
                    this.addToolMenu(jarFile);
                } catch (Exception e) {
                    log.error("加载工具出错：", e);
                }
            }
        }
    }

    private void initEvent() {
    }

    /**
     * 初始化一些按钮信息
     */
    private void initService() {
        ImageView imageView = new ImageView("https://bigfacemonkey0504.oss-cn-hangzhou.aliyuncs.com/openFile.png");
        imageView.setFitWidth(16);
        imageView.setFitHeight(16);
        ImageView imageView2 = new ImageView("https://bigfacemonkey0504.oss-cn-hangzhou.aliyuncs.com/save.png");
        imageView2.setFitWidth(16);
        imageView2.setFitHeight(16);
        ImageView imageView3 = new ImageView("https://bigfacemonkey0504.oss-cn-hangzhou.aliyuncs.com/%E6%93%8D%E4%BD%9C%E6%89%8B%E5%86%8C.png");
        imageView3.setFitWidth(16);
        imageView3.setFitHeight(16);
        ImageView imageView4 = new ImageView("https://bigfacemonkey0504.oss-cn-hangzhou.aliyuncs.com/%E5%85%B3%E4%BA%8E.png");
        imageView4.setFitWidth(16);
        imageView4.setFitHeight(16);
        //打开、另存为
        MenuItem menuItem = new MenuItem("打开(O)",imageView);
        menuItem.setMnemonicParsing(false);
        //设置打开文件快捷键ctrl+o
        menuItem.setAccelerator(KeyCombination.valueOf("Ctrl+O"));
        menuItem.setOnAction((ActionEvent event)->{
            //每次打开，需要清空之前的table对应的数据
            stmtYsDataTable.getColumns().clear();
            //打开文件
            System.out.println("打开文件");
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("请选择文件");
            Stage selectFile = new Stage();
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Excel", "*.xlsx"),
                    new FileChooser.ExtensionFilter("XLS", "*.xls"), new FileChooser.ExtensionFilter("XLSX", "*.xlsx"));
            File file = fileChooser.showOpenDialog(selectFile);
            Set<String> excelHeads = new HashSet<>();
            //TODO excel数据导入显示待补充
            try {
                 excelHeads = PoiExcelUtil.getExcelHeaders(file);
                 //获取excel所有数据，并封装为map类型数据
                List<List<String>> list = PoiExcelUtil.getExcelData(file);
                //将excel数据内容（不包含表头）组装成tableView使用map类型数据需要的数据结构
                analysisResults = PoiExcelUtil.generateMapData(excelHeads,list);
            } catch (Exception e) {
                System.out.println("excel文件标题解决错误");
                e.printStackTrace();
            }
            if(!excelHeads.isEmpty()){
                //获取到excel所有标题
                //设置表头
                for (String head : excelHeads) {
                    headMap.put(head,head);
                    TableColumn tableColumn = new TableColumn(head);
                    tableColumn.setPrefWidth(204);
                    tableColumn.setCellValueFactory(new MapValueFactory(head));
                    stmtYsDataTable.getColumns().add(tableColumn);
                }
                //设置表头之后，设置数据（将组装好的excel数据映射到表中）
                stmtYsDataTable.setItems(analysisResults);
            }
        });
        MenuItem menuItem2 = new MenuItem("另存为(A)",imageView2);
        menuItem2.setMnemonicParsing(false);
        menuItem2.setAccelerator(KeyCombination.valueOf("Ctrl+Shift+S"));
        menuItem2.setOnAction(new EventHandler<ActionEvent>() {
            @SneakyThrows
            @Override
            public void handle(ActionEvent event) {
                System.out.println("另存为");
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("请选择文件");
                Stage selectFile = new Stage();
                fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
                fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Excel", "*.xlsx"),
                        new FileChooser.ExtensionFilter("XLS", "*.xls"), new FileChooser.ExtensionFilter("XLSX", "*.xlsx"));
                File file = fileChooser.showSaveDialog(selectFile);
                if(file!=null){
                    //保存的文件路径不为空
                    PoiExcelUtil.exportXlsx("sheet1",headMap,analysisResults,file);
                }
            }
        });
        //操作手册、关于
        MenuItem menuItem3 = new MenuItem("操作手册(C)",imageView3);
        menuItem3.setMnemonicParsing(false);
        MenuItem menuItem4 = new MenuItem("关于(A)",imageView4);
        menuItem4.setMnemonicParsing(false);
        menuItem4.setOnAction((ActionEvent event)->{
            AlertUtil.showInfoAlert("关于",bundle.getString("aboutText") + Config.xJavaFxToolVersions);
        });
        fileToolMenu.getItems().addAll(menuItem,menuItem2);
        helpMenu.getItems().addAll(menuItem3,menuItem4);
    }

    public void addToolMenu(File file) throws Exception {
        XJavaFxSystemUtil.addJarClass(file);
        Map<String, ToolFxmlLoaderConfiguration> toolMap = new HashMap<>();
        List<ToolFxmlLoaderConfiguration> toolList = new ArrayList<>();

        try (JarFile jarFile = new JarFile(file)) {
            JarEntry entry = jarFile.getJarEntry("config/toolFxmlLoaderConfiguration.xml");
            if (entry == null) {
                return;
            }
            InputStream input = jarFile.getInputStream(entry);
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(input);
            Element root = document.getRootElement();
            List<Element> elements = root.elements("ToolFxmlLoaderConfiguration");
            for (Element configurationNode : elements) {
                ToolFxmlLoaderConfiguration toolFxmlLoaderConfiguration = new ToolFxmlLoaderConfiguration();
                List<Attribute> attributes = configurationNode.attributes();
                for (Attribute configuration : attributes) {
                    BeanUtils.copyProperty(toolFxmlLoaderConfiguration, configuration.getName(), configuration.getValue());
                }
                List<Element> childrenList = configurationNode.elements();
                for (Element configuration : childrenList) {
                    BeanUtils.copyProperty(toolFxmlLoaderConfiguration, configuration.getName(), configuration.getStringValue());
                }
                if (StringUtils.isEmpty(toolFxmlLoaderConfiguration.getMenuParentId())) {
                    toolFxmlLoaderConfiguration.setMenuParentId("moreToolsMenu");
                }
                if (toolFxmlLoaderConfiguration.getIsMenu()) {
                    if (menuMap.get(toolFxmlLoaderConfiguration.getMenuId()) == null) {
                        toolMap.putIfAbsent(toolFxmlLoaderConfiguration.getMenuId(), toolFxmlLoaderConfiguration);
                    }
                } else {
                    toolList.add(toolFxmlLoaderConfiguration);
                }
            }
        }
        toolList.addAll(toolMap.values());
        this.addMenu(toolList);
    }

    private void addMenu(List<ToolFxmlLoaderConfiguration> toolList) {
        for (ToolFxmlLoaderConfiguration toolConfig : toolList) {
            try {
                if (StringUtils.isEmpty(toolConfig.getResourceBundleName())) {
                    if (StringUtils.isNotEmpty(bundle.getString(toolConfig.getTitle()))) {
                        toolConfig.setTitle(bundle.getString(toolConfig.getTitle()));
                    }
                } else {
                    ResourceBundle resourceBundle = ResourceBundle.getBundle(toolConfig.getResourceBundleName(), Config.defaultLocale);
                    if (StringUtils.isNotEmpty(resourceBundle.getString(toolConfig.getTitle()))) {
                        toolConfig.setTitle(resourceBundle.getString(toolConfig.getTitle()));
                    }
                }
            } catch (Exception e) {
                log.error("加载菜单失败", e);
            }
            if (toolConfig.getIsMenu()) {
                Menu menu = new Menu(toolConfig.getTitle());
                if (StringUtils.isNotEmpty(toolConfig.getIconPath())) {
                    ImageView imageView = new ImageView(new Image(toolConfig.getIconPath()));
                    imageView.setFitHeight(18);
                    imageView.setFitWidth(18);
                    menu.setGraphic(imageView);
                }
                menuMap.put(toolConfig.getMenuId(), menu);
            }
        }

        for (ToolFxmlLoaderConfiguration toolConfig : toolList) {
            if (toolConfig.getIsMenu()) {
                menuMap.get(toolConfig.getMenuParentId()).getItems().add(menuMap.get(toolConfig.getMenuId()));
            }
        }

        for (ToolFxmlLoaderConfiguration toolConfig : toolList) {
            if (toolConfig.getIsMenu()) {
                continue;
            }
            MenuItem menuItem = new MenuItem(toolConfig.getTitle());
            if (StringUtils.isNotEmpty(toolConfig.getIconPath())) {
                ImageView imageView = new ImageView(new Image(toolConfig.getIconPath()));
                imageView.setFitHeight(18);
                imageView.setFitWidth(18);
                menuItem.setGraphic(imageView);
            }
            //TODO 注释掉
            if ("Node".equals(toolConfig.getControllerType())) {
                menuItem.setOnAction((ActionEvent event) -> {
                    indexService.addContent(menuItem.getText(), toolConfig.getUrl(), toolConfig.getResourceBundleName(), toolConfig.getIconPath());
                });
                if (toolConfig.getIsDefaultShow()) {
                    indexService.addContent(menuItem.getText(), toolConfig.getUrl(), toolConfig.getResourceBundleName(), toolConfig.getIconPath());
                }
            }
            menuMap.get(toolConfig.getMenuParentId()).getItems().add(menuItem);
            menuItemMap.put(menuItem.getText(), menuItem);
        }
    }

    public void selectAction(String selectText) {
        if (contextMenu.isShowing()) {
            contextMenu.hide();
        }
        contextMenu = indexService.getSelectContextMenu(selectText);
    }

    @FXML
    private void exitAction(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    private void openAllTabAction(ActionEvent event) {
        for (MenuItem value : menuItemMap.values()) {
            value.fire();
        }
    }

    @FXML
    private void pluginManageAction() throws Exception {
        FXMLLoader fXMLLoader = PluginManageController.getFXMLLoader();
        Parent root = fXMLLoader.load();
        PluginManageController pluginManageController = fXMLLoader.getController();
        pluginManageController.setIndexController(this);
        JavaFxViewUtil.openNewWindow(bundle.getString("plugin_manage"), root);
    }


    @FXML
    private void aboutAction(ActionEvent event) throws Exception {
        AlertUtil.showInfoAlert(bundle.getString("aboutText") + Config.xJavaFxToolVersions);
    }

    /**
     * 打开文件
     * @param event
     */
    @FXML
    private void openFile(ActionEvent event) {
        System.out.println("打开文件");

    }

    /**
     * 文件另存为
     * @param event
     */
    @FXML
    private void saveFile(ActionEvent event) {
        System.out.println("文件另存为");
    }

    /**
     * 操作手册
     * @param event
     */
    @FXML
    private void operationManualAction(ActionEvent event){
        System.out.println("操作手册");
    }
}
