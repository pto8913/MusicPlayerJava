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
import pto.FXMLProxy.LoadData;
import pto.Manager.ControllerManager;
import pto.Manager.StageManager;
import pto.Utils.ButtonUtils;

public class MainController implements Initializable
{
    // ---------------------------------------------------------
    // States
    // ---------------------------------------------------------
    // ----------------------------
    // TitleBar States
    // ----------------------------
    @FXML
    private AnchorPane titlePane;
    @FXML
    private Button closeButton;
    @FXML
    private Button minButton;
    @FXML
    private Button menuButton;

    // ----------------------------
    // MainContent States
    // ----------------------------
    @FXML
    private AnchorPane mainContent;
    @FXML
    private AnchorPane sideBarContent;

    // ---------------------------------------------------------
    // Main Functions
    // ---------------------------------------------------------
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        LoadData musicListLoadData = FXMLProxy.loadFXML("MusicList");
        addMainContent(musicListLoadData.parent);
        ControllerManager.addController(musicListLoadData.fxmlLoader.getController());
    }
    public void addMainContent(Node content)
    {
        mainContent.getChildren().add(content);
    }
    public boolean containMainContent(Node content)
    {
        return mainContent.getChildren().contains(content);
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
    private void onDragTitleBar(MouseEvent event)
    {
        Stage stage = (Stage)titlePane.getScene().getWindow();
        stage.setX(event.getScreenX());
        stage.setY(event.getScreenY());
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
        StageManager.removeAll();
        Platform.exit();
    }
    @FXML
    private void onClickedMenuButton(ActionEvent event)
    {
        SideBarController sideBarController = ControllerManager.getController(SideBarController.class);
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
