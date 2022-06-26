package scripts.api.script;

import com.jfoenix.controls.JFXButton;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;
import org.tribot.script.sdk.Log;
import org.tribot.script.sdk.ScriptListening;
import org.tribot.script.sdk.Waiting;
import org.tribot.script.sdk.script.ScriptRuntimeInfo;
import scripts.api.classes.Directory;
import scripts.api.classes.FileHelper;
import scripts.api.classes.GitHub;
import scripts.api.classes.Utility;

import javafx.scene.paint.Color;
import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


/* Written by IvanEOD 6/18/2022, at 8:02 PM */
public class ScriptGui extends Application {

    private final AtomicBoolean showing = new AtomicBoolean(false);
    private final AtomicInteger failedOpenAttempts = new AtomicInteger(0);
    private final String title;
    private final String cssName;
    private final String fxmlName;
    protected ScriptExtension script;
    @Getter
    protected ScriptGuiController controller;
    private File cssFile;
    private Stage stage;
    private Scene scene;
    private AtomicBoolean initialized = new AtomicBoolean(false);

    private GuiScene guiScene;

    private final Runnable onPreEndingListener = this::onPreEnding;
    private final Delta dragDelta = new Delta();

    public ScriptGui(ScriptExtension script) {
        this.script = script;
        this.title = ScriptRuntimeInfo.getScriptName();
        this.cssName = script.getScriptConfiguration().getCssName();
        this.fxmlName = script.getScriptConfiguration().getFxmlName();
        ScriptListening.addPreEndingListener(onPreEndingListener);
        Log.trace("Starting SwingUtilities");
        SwingUtilities.invokeLater(() -> {
            new JFXPanel();
            Platform.runLater(() -> {
                try {
//                    guiScene = new GuiScene();
//                    guiScene.setAlwaysOnTop(true);
//                    start(guiScene);
                    stage = new Stage();
                    stage.initStyle(StageStyle.UNDECORATED);
                    start(stage);
                } catch (Exception e) {
                    Log.error("Error starting GUI", e);
//                    throw new RuntimeException(e);
                }
            });
        });
        waitForInit();
    }



    @Override
    public void start(Stage primaryStage) throws Exception {
//        guiScene.buildContent();

        Log.trace("Starting GUI");
        if (this.fxmlName == null) {
            Log.info("No Gui Detected.");
            return;
        }
        Log.trace("Gui Starting...");

//        UIDefaults uiDefaults = UIManager.getDefaults();
//        uiDefaults.put("activeCaption", new ColorUIResource(Color.DARK_GRAY));
//        uiDefaults.put("activeCaptionText", new ColorUIResource(Color.WHITE));
//        JFrame.setDefaultLookAndFeelDecorated(true);
//
//
//        var toolbar = guiScene.getToolbar();
//        stage = guiScene;
//        scene = guiScene.getScene();
//        scene.setFill(Color.TRANSPARENT);
//
//        toolbar.setOnMousePressed(event -> {
//            dragDelta.x = guiScene.getX() - event.getScreenX();
//            dragDelta.y = guiScene.getY() - event.getScreenY();
//        });
//
//        toolbar.setOnMouseDragged(event -> {
//            guiScene.setX(event.getScreenX() + dragDelta.x);
//            guiScene.setY(event.getScreenY() + dragDelta.y);
//        });

        File fxmlFile = GitHub.getFxml(fxmlName);
        cssFile = GitHub.getCss(cssName);
        if (fxmlFile == null) throw new RuntimeException("Fxml failed to load.");
        if (cssFile == null) throw new RuntimeException("Stylesheet failed to load.");

//        guiScene.setTitle(Utility.toTitleCase(title));
//        guiScene.setResizable(false);
//        guiScene.setOnCloseRequest((event) -> onGuiClosed());
//        guiScene.setOnShown(event -> onGuiOpened());
//        guiScene.getMinimizeButton().setOnAction(event -> guiScene.setIconified(true));
//        guiScene.getCloseButton().setOnAction(event -> guiScene.close());
//        stage.setScene(scene);

        FXMLLoader loader = new FXMLLoader();
        loader.setClassLoader(getClass().getClassLoader());
        Parent box = loader.load(new ByteArrayInputStream(FileHelper.loadFile(fxmlFile)));
        controller = loader.getController();
        if (controller == null) {
            Waiting.wait(1000);
            controller = loader.getController();
            int attempts = 0;
            do {
                attempts++;
                Waiting.wait(1000);
                Log.warn("Waiting for GuiController to load.");
                controller = loader.getController();
            } while (controller == null && attempts < 10);
            if (controller == null) {
                Log.error("Gui controller failed to load.");
                return;
            } else Log.info("Gui Controller loaded.");
        }


        controller.setGui(this);
        controller.setScript(script);
        scene = new Scene(box);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().clear();
        scene.getStylesheets().add(cssFile.toURI().toURL().toExternalForm());
        scene.getRoot().applyCss();
        stage.setScene(scene);
        controller.setScene(scene);
        initialized.set(true);
        Platform.setImplicitExit(false);
    }

    private void waitForInit() {
        Log.trace("Waiting for Gui to load.");
//        while (stage == null || controller == null) {
        while (!initialized.get()) {
            Waiting.wait(250);
        }
        Log.trace("Gui loaded.");
    }

    private void onGuiClosed() {
        showing.set(false);
        script.onGuiClosed();
    }

    private void onGuiOpened() {
        showing.set(true);
        failedOpenAttempts.set(0);
        script.onGuiOpened();
    }


    public void show() {
        if (stage == null) {
            Log.warn("Cannot open Gui, stage is null.");
            return;
        }
        if (isShowing()) {
            Log.warn("Gui open was called but the gui is already showing.");
            return;
        }

        AtomicBoolean attempted = new AtomicBoolean(false);
        Platform.runLater(() -> {
            stage.show();
            stage.toFront();
            attempted.set(true);
        });
        if (Waiting.waitUntil(3000, attempted::get)) onGuiOpened();
        else {
            var fails = failedOpenAttempts.incrementAndGet();
            if (fails >= 5) {
                Log.error("Failed to open Gui 5 times, quitting script.");
                script.quit();
            } else show();
        }
    }

    public void close() {
        if (!isShowing()) {
            Log.warn("Gui close was called but the gui is not showing..");
            return;
        }
        onGuiClosed();
        if (stage == null) return;
        Platform.runLater(() -> stage.close());
    }

    public boolean isShowing() {
        return showing.get();
    }

    public void startScript() {
        script.onGuiStartButton();
    }

    private void onPreEnding() {
        ScriptListening.removePreEndingListener(onPreEndingListener);
        close();
        try {
            stop();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private static class WindowButtons extends HBox {

        public WindowButtons() {
            JFXButton closeButton = new JFXButton("X");
            JFXButton minimizeButton = new JFXButton("_");
            this.setAlignment(Pos.CENTER_RIGHT);
            this.setSpacing(1);
            closeButton.setOnAction(event -> {
                Platform.exit();
            });
            minimizeButton.setOnAction(event -> {
                Platform.runLater(() -> {
                    try {
                        var stage = (Stage) minimizeButton.getScene().getWindow();
                        stage.setIconified(true);
                    } catch (Exception e) {
                        Log.error("Error minimizing window.");
                    }
                });
            });
            this.getChildren().addAll(minimizeButton, closeButton);

        }


    }

    private static class Delta {
        private double x;
        private double y;
    }


}
