package pto.Controller;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import pto.Utils.ButtonUtils;

/*
 * Create once in MainController
 */
public class InputNameController implements IFloatingController
{
    // ---------------------------------------------------------
    // States
    // ---------------------------------------------------------
    @FXML
    private AnchorPane entire;
    @FXML
    private Button acceptButton;
    @FXML
    private TextField nameTextField;

    // ---------------------------------------------------------
    // Main Functions
    // ---------------------------------------------------------
    public void setOnAction(EventHandler<ActionEvent> in)
    {
        acceptButton.setOnAction(in);
    }
    public String getNameText()
    {
        return nameTextField.getText();
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

    // ---------------------------------------------------------
    // IFloatingController Functions
    // ---------------------------------------------------------
    public double getOpenTranslate()
    {
        final Pane parent = (Pane)entire.getParent();
        return parent.getHeight() - entire.getPrefHeight();
    }
    public double getCloseTranslate()
    {
        final Pane parent = (Pane)entire.getParent();
        return parent.getHeight();
    }

    @Override
    public boolean isOpen()
    {
        return entire.getTranslateX() == getOpenTranslate();
    }
    @Override
    public void playOpenAnimation()
    {
        TranslateTransition openNav = new TranslateTransition(new Duration(200), entire);
        openNav.setToY(getOpenTranslate());
        openNav.play();
    }
    @Override
    public void playCloseAnimation()
    {
        TranslateTransition closeNav = new TranslateTransition(new Duration(100), entire);
        closeNav.setToY(getCloseTranslate());
        closeNav.play();
    }
    @Override
    public boolean isIgnoreClose()
    {
        return false;
    }

}
