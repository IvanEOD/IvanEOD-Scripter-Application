package scripts.appApi.gui.scenes;

import javafx.scene.Scene;
import lombok.Data;
import scripts.appApi.script.GuiScene;
import scripts.appApi.script.ScriptGuiController;

import java.util.ArrayList;
import java.util.List;

/* Written by IvanEOD 6/26/2022, at 7:50 PM */
public class SceneLoader {

    private final List<SceneData> scenes = new ArrayList<>();


    private SceneLoader() {}

    public static SceneLoader getInstance() {
        return SceneLoaderInstance.INSTANCE;
    }

    private static final class SceneLoaderInstance {
        private static final SceneLoader INSTANCE = new SceneLoader();
    }

    @Data
    private static class SceneData {
        private final String name;
        private final Scene scene;
        private final ScriptGuiController controller;
    }

    @Data
    private static class WindowData {
        private final String name;
        private final GuiScene scene;
        private final ScriptGuiController controller;
    }


    //        SwingUtilities.invokeLater(() -> {
    //            new JFXPanel();
    //            Platform.runLater(() -> {
    //                try {
    //                    guiScene = new GuiScene();
    //                    guiScene.setAlwaysOnTop(true);
    //                    start(guiScene);
    //                } catch (Exception e) {
    //                    Log.error("Error starting GUI", e);
    ////                    throw new RuntimeException(e);
    //                }
    //            });
    //        });

}
