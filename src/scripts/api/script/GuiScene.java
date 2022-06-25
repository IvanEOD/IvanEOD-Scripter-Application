package scripts.api.script;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/* Written by IvanEOD 6/25/2022, at 4:07 PM */
public class GuiScene extends Scene {

    private final BorderPane root;

    public GuiScene() {
        super(new BorderPane());
        root = (BorderPane) getRoot();

        var toolbar = new ToolBar();
        toolbar.setStyle("-fx-background-color: #2e2e2e;");
        toolbar.setPrefHeight(30);
        toolbar.setPrefWidth(getWidth());

        JFXButton closeButton = new JFXButton("X");
        closeButton.setStyle("-fx-background-color: #2e2e2e;");
        closeButton.setTextFill(new Color(0.9, 0.9, 0.9, 1));
        closeButton.setOnAction(e -> Platform.exit());
        closeButton.setPrefSize(30, 30);
        closeButton.setAlignment(Pos.CENTER);

        JFXButton minimizeButton = new JFXButton("_");
        minimizeButton.setStyle("-fx-background-color: #2e2e2e;");
        minimizeButton.setTextFill(new Color(0.9, 0.9, 0.9, 1));
        minimizeButton.setOnAction(e -> ((Stage) minimizeButton.getScene().getWindow()).setIconified(true));
        minimizeButton.setPrefSize(30, 30);
        minimizeButton.setAlignment(Pos.CENTER);

        toolbar.getItems().addAll(closeButton, minimizeButton);


    }




}
