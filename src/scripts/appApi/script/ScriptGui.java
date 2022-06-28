package scripts.appApi.script;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import org.tribot.script.sdk.Log;
import org.tribot.script.sdk.ScriptListening;
import org.tribot.script.sdk.Waiting;
import org.tribot.script.sdk.script.ScriptRuntimeInfo;
import scripts.appApi.classes.FileHelper;
import scripts.appApi.classes.GitHub;
import scripts.appApi.classes.Utility;

import javax.swing.*;
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
                    guiScene = new GuiScene();
                    guiScene.setAlwaysOnTop(true);
                    start(guiScene);
                } catch (Exception e) {
                    Log.error("Error starting GUI", e);
                }
            });
        });
        waitForInit();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Log.trace("Starting GUI");
        if (this.fxmlName == null) {
            Log.info("No Gui Detected.");
            return;
        }
        Log.trace("Gui Starting...");

        guiScene.buildContent();
        stage = guiScene;
        scene = guiScene.getScene();

        File fxmlFile = GitHub.getFxml(fxmlName);
        cssFile = GitHub.getCss(cssName);
        if (fxmlFile == null) throw new RuntimeException("Fxml failed to load.");
        if (cssFile == null) throw new RuntimeException("Stylesheet failed to load.");

        stage.setTitle(Utility.toTitleCase(title));
        stage.setOnCloseRequest((event) -> onGuiClosed());
        stage.setOnShown(event -> onGuiOpened());

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

        guiScene.getContent().getChildren().add(box);
        controller.setGui(this);
        controller.setScript(script);
        scene.getStylesheets().add(cssFile.toURI().toURL().toExternalForm());
        scene.getRoot().applyCss();
        stage.setScene(scene);
        controller.setScene(scene);
        initialized.set(true);
        Platform.setImplicitExit(false);
    }

    private void waitForInit() {
        Log.trace("Waiting for Gui to load.");
        while (!initialized.get()) Waiting.wait(250);
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


}
