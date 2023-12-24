package pto.Controller;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import pto.Constants.PtoSettings;
import pto.Events.SoundPlayerEvent;
import pto.Events.SoundPlayerEvent.SoundEventTypes;
import pto.Manager.AppInstance;
import pto.Manager.SoundManager;

/*
 * Create once in MainController
 */
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
    private Button musicPlayButton;
    @FXML
    private Polygon musicPlayPolygon;
    @FXML
    private Rectangle musicStopPolygon;
    private boolean isPlaying = false;
    @FXML
    private Button musicNextButton;

    @FXML
    private Label musicTitleLabel;
    @FXML
    private ImageView musicThumbnail;
    
    @FXML
    private Slider playTimeSlider;
    private boolean isPlayTimeSliderMoved = false;

    @FXML
    private Button volumeButton;
    @FXML
    private Slider volumeSlider;
    private boolean isVolumeSliderMoved = false;
    @FXML
    private Label volumeLabel;

    private int activeIndex = 0;

    // ---------------------------------------------------------
    // Main Functions
    // ---------------------------------------------------------
    public UserController()
    {
    }

    public void setMusicData(int activeIndex, String title)
    {
        if (!AppInstance.get().getSoundManager().isPlaying())
        {
            this.activeIndex = activeIndex;
            musicTitleLabel.setText(title);

            MusicListController controller = AppInstance.get().getControllerManager().getMusicListController();
            controller.musicLinkList.getSelectionModel().clearAndSelect(activeIndex);
        }
    }
    public String getActiveMusicTitle()
    {
        return musicTitleLabel.getText();
    }

    public void resetState()
    {
        musicPlayPolygon.setStyle("-fx-opacity: 1");
        musicStopPolygon.setStyle("-fx-opacity: 0");
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
            AppInstance.get().getSoundManager().Play(activeIndex);

            AppInstance.get().getSoundManager().setSoundPlayerEvent(
                getClass().getName() + "_PlayTime",
                soundPlayerEvent -> {
                    onPlayTimeChanged(soundPlayerEvent);
                }
            );
            AppInstance.get().getSoundManager().setSoundPlayerEvent(
                getClass().getName() + "_MusicEnd", 
                soundPlayerEvent -> {
                    onMusicEnd(soundPlayerEvent);
                }
            );
        }
        else
        {
            musicPlayPolygon.setStyle("-fx-opacity: 1");
            musicStopPolygon.setStyle("-fx-opacity: 0");
            AppInstance.get().getSoundManager().Stop();
        }
    }
    @FXML
    private void onClickedPrevButton(ActionEvent event)
    {
        SoundManager.SoundPlayerData newMusicData = AppInstance.get().getSoundManager().Prev();
        setMusicData(newMusicData.activeIndex, newMusicData.musicData.getName());
        MusicListController controller = AppInstance.get().getControllerManager().getMusicListController();
        controller.musicLinkList.scrollTo(activeIndex);
    }
    @FXML
    private void onClickedNextButton(ActionEvent event)
    {
        SoundManager.SoundPlayerData newMusicData = AppInstance.get().getSoundManager().Next();
        setMusicData(newMusicData.activeIndex, newMusicData.musicData.getName());
        MusicListController controller = AppInstance.get().getControllerManager().getMusicListController();
        controller.musicLinkList.scrollTo(activeIndex);
    }
    
    // ----------------------------
    // Main Functions : Music
    // ----------------------------
    private void onMusicEnd(SoundPlayerEvent event)
    {
        if (event.getType() == SoundEventTypes.End)
        {
            musicTitleLabel.setText(event.getMusicTitle());

            MusicListController controller = AppInstance.get().getControllerManager().getMusicListController();
            controller.musicLinkList.getSelectionModel().clearAndSelect(activeIndex);
        }
    }

    // ----------------------------
    // Main Functions : Volume
    // ----------------------------
    @FXML
    private void onClickedVolumeButton(ActionEvent event)
    {
        if (volumeSlider.isVisible())
        {
            volumeSlider.setVisible(false);
            volumeLabel.setVisible(false);
        }
        else
        {
            volumeSlider.setVisible(true);
            volumeLabel.setVisible(true);
        }
    }
    @FXML
    private void onVolumeSliderMoveStart()
    {
        isVolumeSliderMoved = true;
    }
    @FXML
    private void onVolumeSliderDragged()
    {
        if (isVolumeSliderMoved)
        {
            updateVolume();
        }
    }
    @FXML
    private void onVolumeSliderChanged()
    {
        updateVolume();
        isVolumeSliderMoved = false;
    }
    private void updateVolume()
    {
        final double sliderValue = volumeSlider.getValue();
        AppInstance.get().getSoundManager().setVolume(sliderValue);
        volumeLabel.setText(String.valueOf((int)sliderValue));
        AppInstance.get().getMusicJsonManager().setVolume(sliderValue);
    }

    // ----------------------------
    // Main Functions : Play Time
    // ----------------------------
    private void onPlayTimeChanged(SoundPlayerEvent event)
    {
        if (event.getType() == SoundEventTypes.Duration)
        {
            playTimeSlider.setValue(event.getTimePercent() * 100);
        }
    }
    @FXML
    private void onPlayTimeSliderMoveStart()
    {
        isPlayTimeSliderMoved = true;
    }
    @FXML
    private void onPlayTimeSliderDragged()
    {
        if (isPlayTimeSliderMoved)
        {
            updatePlayTime(false);
        }
    }
    @FXML
    private void onPlayTimeSliderChanged()
    {
        isPlayTimeSliderMoved = false;
        updatePlayTime(true);
    }
    private void updatePlayTime(boolean isToPlay)
    {
        final double currentPlayTime = playTimeSlider.getValue();
        AppInstance.get().getSoundManager().setPlayTime(currentPlayTime / 100, isToPlay);
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
            button.setStyle("-fx-background-color: rgba(255,255,255,0.25)");
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
            button.setStyle("-fx-background-color: rgba(255,255,255,0.5)");
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
    @Override
    public boolean isOpen()
    {
        return userControlMenu.getTranslateX() == getOpenTranslate();
    }
    @Override
    public void playOpenAnimation()
    {
        final double volume = AppInstance.get().getMusicJsonManager().getVolume();
        if (volume != 0)
        {
            volumeSlider.setValue(volume);
            AppInstance.get().getSoundManager().setVolume(volume);
            volumeLabel.setText(String.valueOf((int)volume));
        }

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
    @Override
    public boolean isIgnoreClose()
    {
        return false;
    }
}