package pto.Controller;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import pto.Controller.ListView.MusicData;
import pto.Manager.SoundPlayer;

public class UserController implements IFloatingController
{
    // ---------------------------------------------------------
    // States
    // ---------------------------------------------------------

    // ----------------------------
    // States : UserControl
    // ----------------------------
    @FXML
    private AnchorPane userControlMenu;
    @FXML
    private Button musicPrevButton;
    @FXML
    private Button musicBackButton;
    @FXML
    private Button musicPlayButton;
    @FXML
    private Polygon musicPlayPolygon;
    @FXML
    private Rectangle musicStopPolygon;
    private boolean isPlaying = false;
    @FXML
    private Button musicForwardButton;
    @FXML
    private Button musicNextButton;

    @FXML
    private Label musicTitleLabel;

    @FXML
    private ImageView musicThumbnail;

    private MusicData musicData;

    // ---------------------------------------------------------
    // Main Functions
    // ---------------------------------------------------------
    public void setMusicData(MusicData musicData)
    {
        this.musicData = musicData;
        musicTitleLabel.setText(musicData.getName());
    }
    public MusicData getMusicData()
    {
        return musicData;
    }
    
    public double getOpenTranslate()
    {
        final Pane parent = (Pane)userControlMenu.getParent();
        return parent.getHeight() - userControlMenu.getPrefHeight();
    }
    public double getCloseTranslate()
    {
        final Pane parent = (Pane)userControlMenu.getParent();
        return parent.getHeight();
    }

    // ----------------------------
    // Main Functions : UserControl
    // ----------------------------
    @FXML
    private void onClickedPlayButton(ActionEvent event)
    {
        isPlaying = !isPlaying;
        if (isPlaying)
        {
            musicPlayPolygon.setStyle("-fx-opacity: 0");
            musicStopPolygon.setStyle("-fx-opacity: 1");
            SoundPlayer.Play(musicData.getPath());
        }
        else
        {
            musicPlayPolygon.setStyle("-fx-opacity: 1");
            musicStopPolygon.setStyle("-fx-opacity: 0");
            SoundPlayer.Stop();
        }
    }
    @FXML
    private void onClickedBackButton(ActionEvent event)
    {
        SoundPlayer.JumpBack(10.0);
    }
    @FXML
    private void onClickedForwardButton(ActionEvent event)
    {
        SoundPlayer.Jump(10.0);
    }
    @FXML
    private void onClickedPrevButton(ActionEvent event)
    {
    }
    @FXML
    private void onClickedNextButton(ActionEvent event)
    {
    }
   
    // ----------------------------
    // Main Functions : Button Utility
    // ----------------------------
    @FXML
    private void onMouseHovered(MouseEvent event)
    {
        Button button = (Button)event.getSource();
        if (button != null)
        {
            button.setStyle("-fx-background-color: rgba(50,50,50,0.25)");
        }
    }
    @FXML
    private void onMouseLeave(MouseEvent event)
    {
        Button button = (Button)event.getSource();
        if (button != null)
        {
            button.setStyle("-fx-background-color: rgba(255,255,255,0)");
        }
    }
    @FXML
    private void onMousePressed(MouseEvent event)
    {
        Button button = (Button)event.getSource();
        if (button != null)
        {
            button.setStyle("-fx-background-color: rgba(50,50,50,0.5)");
        }
    }
    @FXML
    private void onMouseReleased(MouseEvent event)
    {
        Button button = (Button)event.getSource();
        if (button != null)
        {
            button.setStyle("-fx-background-color: rgba(255,255,255,0.25)");
        }
    }

    // ---------------------------------------------------------
    // IFloatingController Functions
    // ---------------------------------------------------------
    @Override
    public boolean isOpen()
    {
        return userControlMenu.getTranslateX() == getOpenTranslate();
    }
    @Override
    public void playOpenAnimation()
    {
        TranslateTransition openNav = new TranslateTransition(new Duration(200), userControlMenu);
        openNav.setToY(getOpenTranslate());
        openNav.play();
    }
    @Override
    public void playCloseAnimation()
    {
        TranslateTransition closeNav = new TranslateTransition(new Duration(100), userControlMenu);
        closeNav.setToY(getCloseTranslate());
        closeNav.play();
    }
}