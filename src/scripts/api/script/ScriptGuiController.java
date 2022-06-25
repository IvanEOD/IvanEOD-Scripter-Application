package scripts.api.script;

import javafx.fxml.Initializable;
import javafx.scene.Scene;
import lombok.Setter;

import java.net.URL;
import java.util.ResourceBundle;

/* Written by IvanEOD 6/18/2022, at 8:03 PM */
public class ScriptGuiController implements Initializable {

    protected ScriptGui gui;
    protected ScriptExtension script;

    @Setter
    protected Scene scene;

    public ScriptGui getGui() {
        return gui;
    }

    public void setGui(ScriptGui gui) {
        this.gui = gui;
    }

    public void setScript(ScriptExtension script) {
        this.script = script;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
