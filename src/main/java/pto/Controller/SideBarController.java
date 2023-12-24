package pto.Controller;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import pto.Constants.PtoSettings;
import pto.Controller.ListView.MusicListTypes;
import pto.Manager.AppInstance;
import pto.Utils.ButtonUtils;

/*
 * Create once in MainController.
 */
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
        AppInstance.get().getControllerManager().addController(this);
    }

    @FXML
    private void onClickedOpenMusicList(ActionEvent event)
    {
        MusicListController musicListController = AppInstance.get().getControllerManager().getController(PtoSettings.MLCONTROLLER_MUSICLIST);
        AppInstance.get().getControllerManager().closeAllFloatingController();
        musicListController.playOpenAnimation();
        AppInstance.get().getStateManager().setMusicListMode(MusicListTypes.MusicListMode.MusicList);
    }
    @FXML
    private void onClickedOpenMusicPlayList(ActionEvent event)
    {
        AppInstance.get().getControllerManager().openMusicListController(PtoSettings.MLCONTROLLER_PLAYLIST, MusicListTypes.MusicListMode.PlayList);
    }

    // ---------------------------------------------------------
    // IFloatingController Functions
    // ---------------------------------------------------------
    public double getOpenTranslate()
    {
        return 0;//navList.getPrefWidth();
    }
    public double getCloseTranslate()
    {
        return -navList.getPrefWidth();
    }
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
    @Override
    public boolean isIgnoreAllClose()
    {
        return false;
    }
    @Override
    public void setIgnoreAllClose(boolean in)
    {
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
