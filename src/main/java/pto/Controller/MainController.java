package pto.Controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import pto.FXMLProxy;
import pto.Constants.PtoSettings;
import pto.Controller.ListView.MusicListTypes;
import pto.Controller.ListView.MusicListTypes.MusicListMode;
import pto.FXMLProxy.LoadData;
import pto.Manager.AppInstance;
import pto.Utils.ButtonUtils;

/*
 * Create once in App
 */
public class MainController implements Initializable
{
    // ---------------------------------------------------------
    // States
    // ---------------------------------------------------------
    // ----------------------------
    // TitleBar States
    // ----------------------------
    @FXML
    private AnchorPane mainPane;
    @FXML
    private AnchorPane titlePane;
    @FXML
    private Button closeButton;
    @FXML
    private Button minButton;
    @FXML
    private Button menuButton;
    private double titleBarOffsetX;
    private double titleBarOffsetY;

    // ----------------------------
    // MainContent States
    // ----------------------------
    @FXML
    private AnchorPane mainContent;
    @FXML
    private AnchorPane sideBarContent;
    @FXML
    private AnchorPane listMenuPane;

    // ---------------------------------------------------------
    // Main Functions
    // ---------------------------------------------------------
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        /* Initialize ListMenuBarController */
        {
            LoadData loadData = FXMLProxy.loadFXML("ListMenuBar");
            listMenuPane.getChildren().add(loadData.parent);
            ListMenuBarController listMenuBarController = loadData.fxmlLoader.getController();
            AppInstance.get().getControllerManager().addController(listMenuBarController);
        }
        /* Initialize UserController */
        {
            LoadData loadData = FXMLProxy.loadFXML("UserControl");
            UserController userController = loadData.fxmlLoader.getController();
            AppInstance.get().getControllerManager().addController(userController);
            
            mainPane.getChildren().add(loadData.parent);
            loadData.parent.setTranslateY(mainPane.getPrefHeight());
        }
        /* Initialize MusicListController */
        {
            LoadData loadData = FXMLProxy.loadFXML("MusicList", new MusicListTypes(MusicListMode.MusicList));
            addMainContent(loadData.parent);
            MusicListController musicListController = loadData.fxmlLoader.getController();
            AppInstance.get().getControllerManager().addController(PtoSettings.MLCONTROLLER_MUSICLIST, musicListController);
        }
        /* Initialize InputNameController */
        {
            LoadData loadData = FXMLProxy.loadFXML("InputName");
            InputNameController inputNameController = loadData.fxmlLoader.getController();
            AppInstance.get().getControllerManager().addController(inputNameController);

            mainPane.getChildren().add(loadData.parent);
            loadData.parent.setTranslateY(mainPane.getPrefHeight());
        }
    }
    public void addMainContent(Node content)
    {
        mainContent.getChildren().add(content);
    }
    public boolean containMainContent(Node content)
    {
        return mainContent.getChildren().contains(content);
    }
    public void removeMainContent(Node content)
    {
        mainContent.getChildren().remove(content);
    }
    public void addSideContent(Node content)
    {
        sideBarContent.getChildren().add(content);
    }
    public boolean containSubContent(Node content)
    {
        return sideBarContent.getChildren().contains(content);
    }
   
    // ----------------------------
    // TitleBar Functions
    // ----------------------------
    @FXML
    private void onPressedTitleBar(MouseEvent event)
    {
        titleBarOffsetX = event.getSceneX();
        titleBarOffsetY = event.getSceneY();
    }
    @FXML
    private void onDragTitleBar(MouseEvent event)
    {
        Stage stage = (Stage)titlePane.getScene().getWindow();
        stage.setX(event.getScreenX() - titleBarOffsetX);
        stage.setY(event.getScreenY() - titleBarOffsetY);
    }
    @FXML
    private void onClickedMinimizeButton(ActionEvent event)
    {
        Stage stage = (Stage)minButton.getScene().getWindow();
        stage.setIconified(true);
    }
    @FXML
    private void onClickedCloseButton(ActionEvent event)
    {
        Platform.exit();
    }
    @FXML
    private void onClickedMenuButton(ActionEvent event)
    {
        SideBarController sideBarController = AppInstance.get().getControllerManager().getController(SideBarController.class);
        if (sideBarController == null)
        {
            LoadData loadData = FXMLProxy.loadFXML("SideBar");
            sideBarController = loadData.fxmlLoader.getController();

            sideBarContent.getChildren().add(loadData.parent);
            loadData.parent.setTranslateX(sideBarController.getCloseTranslate());
        }
        if (sideBarController.isOpen())
        {
            sideBarController.playCloseAnimation();
        }
        else
        {
            sideBarController.playOpenAnimation();
        }
    }

    // ----------------------------
    // Button Utility Functions
    // ----------------------------
    @FXML
    private void onMouseHovered(MouseEvent event)
    {
        ButtonUtils.onMouseHovered(event);
    }
    @FXML
    private void onMouseLeave(MouseEvent event)
    {
        ButtonUtils.onMouseLeave(event);
    }
    @FXML
    private void onMousePressed(MouseEvent event)
    {
        ButtonUtils.onMousePressed(event);
    }
    @FXML
    private void onMouseReleased(MouseEvent event)
    {
        ButtonUtils.onMouseReleased(event);
    }
}
