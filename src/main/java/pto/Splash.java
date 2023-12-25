package pto;

import javafx.animation.*;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.io.IOException;


/**
 * This is my own splash screen, that I made myself.
 *
 */
public class Splash
{
    static Scene splash;
    static Rectangle rect = new Rectangle();
    final private Pane pane;
    final private SequentialTransition seqT;

    public Splash()
    {
        pane = new Pane();
        pane.setStyle("-fx-background-color:black");
        pane.setPrefWidth(256);
        pane.setPrefHeight(256);

        splash = new Scene(pane);
        seqT = new SequentialTransition();
    }

    public void show() throws IOException
    {
        /*
         * Part 1:
         * This is the rolling square animation.
         * This animation looks cool for a loading screen,
         * so I made this. Only the lines of code for fading
         * from Stack Overflow.
         */
        //rectangle insertion
        int scale = 8;
        int dur = 500;
        rect = new Rectangle(100 - 2 * scale, 20, scale, scale);
        rect.setFill(Color.BLACK);
        rect.setX(74);
        rect.setY(238);

        //actual animations
        //initialising the sequentialTranslation
        //umm, ignore this, just some configs that work magic
        int[] rotins = {scale, 2 * scale, 3 * scale, 4 * scale, 5 * scale, -6 * scale, -5 * scale, -4 * scale, -3 * scale, -2 * scale};
        int x, y;
        for (int i : rotins) {
            //rotating the square
            RotateTransition rt = new RotateTransition(Duration.millis(dur), rect);
            rt.setByAngle(i / Math.abs(i) * 90);
            rt.setCycleCount(1);
            //moving the square horizontally
            TranslateTransition pt = new TranslateTransition(Duration.millis(dur), rect);
            x = (int) (rect.getX() + Math.abs(i));
            y = (int) (rect.getX() + Math.abs(i) + (Math.abs(i) / i) * scale);
            pt.setFromX(x);
            pt.setToX(y);
            //parallelly execute them and you get a rolling square
            ParallelTransition pat = new ParallelTransition();
            pat.getChildren().addAll(pt, rt);
            pat.setCycleCount(1);
            seqT.getChildren().add(pat);
        }
        //playing the animation
        seqT.play();
        //lambda code sourced from StackOverflow, fades away stage
        seqT.setNode(rect);
        //The text part
        Label launchLabel = new Label("Launching");
        launchLabel.setFont(new Font("Times New Roman", 20));
        launchLabel.setStyle("-fx-text-fill:black");
        launchLabel.setLayoutX(64);
        launchLabel.setLayoutY(226);
        //A complimentary image

        Image image = new Image(App.class.getResource("pto8913.png").toString());
        ImageView iv = new ImageView(image);
        iv.setFitWidth(256);
        iv.setFitHeight(256);
//        iv.setX(150 - 64);
//        iv.setY(100 - 64);
        //now adding everything to position, opening the stage, start the animation
        pane.getChildren().addAll(iv, rect, launchLabel);

        seqT.play();
    }

    public Scene getSplashScene()
    {
        return splash;
    }

    public SequentialTransition getSequentialTransition()
    {
        return seqT;
    }
}