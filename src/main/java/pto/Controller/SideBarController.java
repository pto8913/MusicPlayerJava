package pto.Controller;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import pto.FXMLProxy;
import pto.FXMLProxy.LoadData;
import pto.Controller.ListView.MusicListTypes.MusicListMode;
import pto.Manager.ControllerManager;
import pto.Manager.StateManager;
import pto.Utils.ButtonUtils;

public class SideBarController implements IFloatingController
{
    // ---------------------------------------------------------
    // States
    // ---------------------------------------------------------

    // ----------------------------
    // SideBar States
    // ----------------------------
    @FXML
    private AnchorPane navList;
    @FXML
    private Button musicListButton;
    @FXML
    private Button musicPlayListButton;

    // ---------------------------------------------------------
    // Main Functions
    // ---------------------------------------------------------
    @FXML
    public void initialize()
    {
        ControllerManager.addController(this);
    }

    public double getOpenTranslate()
    {
        return 0;//navList.getPrefWidth();
    }
    public double getCloseTranslate()
    {
        return -navList.getPrefWidth();
    }

    @FXML
    private void onClickedOpenMusicList(ActionEvent event)
    {
        openMusicList();
        StateManager.setMusicListMode(MusicListMode.MusicList);
    }
    @FXML
    private void onClickedOpenMusicPlayList(ActionEvent event)
    {
        openMusicList();
        StateManager.setMusicListMode(MusicListMode.PlayList);
    }

    private void openMusicList()
    {
        MainController mainController = ControllerManager.getController(MainController.class);
        LoadData loadData = FXMLProxy.loadFXML("MusicList");
        if (!mainController.containMainContent(loadData.parent))
        {
            mainController.addMainContent(loadData.parent);
            ControllerManager.closeAllFloatingController();
        }
    }

    // ---------------------------------------------------------
    // IFloatingController Functions
    // ---------------------------------------------------------
    @Override
    public boolean isOpen()
    {
        return navList.getTranslateX() == getOpenTranslate();
    }
    @Override
    public void playOpenAnimation()
    {
        TranslateTransition openNav = new TranslateTransition(new Duration(200), navList);
        openNav.setToX(getOpenTranslate());
        openNav.play();
    }
    @Override
    public void playCloseAnimation()
    {
        TranslateTransition closeNav = new TranslateTransition(new Duration(100), navList);
        closeNav.setToX(getCloseTranslate());
        closeNav.play();
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
