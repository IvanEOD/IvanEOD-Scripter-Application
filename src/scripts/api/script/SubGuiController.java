package scripts.api.script;

import javafx.fxml.Initializable;
import javafx.scene.Scene;
import lombok.Setter;

import java.net.URL;
import java.util.ResourceBundle;

/* Written by IvanEOD 6/26/2022, at 8:04 PM */
public class SubGuiController implements Initializable {


    protected GuiFrame gui;

    @Setter
    protected Scene scene;

    public GuiFrame getGui() {
        return gui;
    }

    public void setGui(GuiFrame gui) {
        this.gui = gui;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setupComponents();
    }

    public void setupComponents() {

    }

    public void onGuiClosed() {

    }

    public void onGuiOpened() {

    }


}
