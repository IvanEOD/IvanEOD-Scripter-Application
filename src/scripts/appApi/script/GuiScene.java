package scripts.appApi.script;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;
import scripts.appApi.classes.GitHub;

/* Written by IvanEOD 6/25/2022, at 4:07 PM */
@Getter
public class GuiScene extends Stage {

    private VBox root;
    private ToolBar toolbar;
    private JFXButton minimizeButton;
    private JFXButton closeButton;
    private final Delta dragDelta = new Delta();

    private AnchorPane content;

    public GuiScene() {
        super();
    }

    public void buildContent() {

        setTitle("IvanEOD Scripts");
        setResizable(false);

        root = new VBox();

        root.getStyleClass().add("main-window");

        //<editor-fold desc="Toolbar">
        toolbar = new ToolBar();
        toolbar.setPrefSize(600, 20);
        toolbar.setMaxSize(600, 20);

        var toolbarHbox = new HBox(2);
        toolbarHbox.getStyleClass().add("tool-bar");
        toolbarHbox.setAlignment(Pos.CENTER_RIGHT);
        toolbarHbox.setPadding(new Insets(4));
        toolbarHbox.setPrefSize(588, 20);

        var titleLogo = new ImageView();
        titleLogo.setFitHeight(21);
        titleLogo.setFitWidth(21);
        titleLogo.setPreserveRatio(true);
        titleLogo.setSmooth(true);
        Image logo = GitHub.getImage("tribotLogo");
        titleLogo.setImage(logo);

        var titleLabel = new Label("IvanEOD Scripts");
        titleLabel.getStyleClass().add("title");
        titleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) titleLabel.setText("IvanEOD Scripts  -  " + newValue);
            else titleLabel.setText("IvanEOD Scripts");
        });

        var toolbarLeft = new HBox(5, titleLogo, titleLabel);
        toolbarLeft.setPadding(new Insets(0));
        toolbarLeft.setAlignment(Pos.CENTER_LEFT);
        toolbarLeft.setPrefSize(550, 21);
//        toolbarLeft.setStyle("-fx-background-color: transparent;");

        closeButton = new JFXButton("X");
        closeButton.setButtonType(JFXButton.ButtonType.RAISED);
        closeButton.setCancelButton(true);
        closeButton.setPrefSize(15, 15);
        closeButton.setMaxSize(15, 15);
        closeButton.getStyleClass().add("close-button");
        closeButton.setOnAction(e -> Platform.exit());
        closeButton.setAlignment(Pos.CENTER);

        minimizeButton = new JFXButton("_");
        minimizeButton.setButtonType(JFXButton.ButtonType.RAISED);
        minimizeButton.setPrefSize(15, 15);
        minimizeButton.setMaxSize(15, 15);
        minimizeButton.getStyleClass().add("minimize-button");
        minimizeButton.setOnAction(e -> ((Stage) minimizeButton.getScene().getWindow()).setIconified(true));
        minimizeButton.setAlignment(Pos.CENTER);

        minimizeButton.setOnAction(event -> setIconified(true));
        closeButton.setOnAction(event -> close());

        var toolbarRight = new HBox(1, minimizeButton, closeButton);
        toolbarRight.setPrefSize(38, 21);
        toolbarRight.setMaxSize(38, 21);
        toolbarRight.setPadding(new Insets(1));

        toolbarHbox.getChildren().addAll(toolbarLeft, toolbarRight);
        toolbar.getItems().add(toolbarHbox);

        toolbar.setOnMousePressed(event -> {
            dragDelta.x = getX() - event.getScreenX();
            dragDelta.y = getY() - event.getScreenY();
        });

        toolbar.setOnMouseDragged(event -> {
            setX(event.getScreenX() + dragDelta.x);
            setY(event.getScreenY() + dragDelta.y);
        });

        //</editor-fold>

        //<editor-fold desc="Content Area">
        var menubar = new MenuBar();

        var fileMenu = new Menu("File");
        var fileMenuItem = new MenuItem("File Menu Item");
        fileMenu.getItems().add(fileMenuItem);

        var editMenu = new Menu("Edit");
        var editMenuItem = new MenuItem("Edit Menu Item");
        editMenu.getItems().add(editMenuItem);

        var viewMenu = new Menu("View");
        var viewMenuItem = new MenuItem("View Menu Item");
        viewMenu.getItems().add(viewMenuItem);
        menubar.getMenus().addAll(fileMenu, editMenu, viewMenu);
        menubar.setMaxWidth(600);
        content = new AnchorPane();
        content.setPrefSize(600, 380);
        content.setMaxSize(600, 380);
        content.getStyleClass().add("content-pane");

        var contentArea = new VBox(menubar, content);
        contentArea.setPrefSize(600, 400);
        contentArea.setMaxSize(600, 400);
        //</editor-fold>

        root.getChildren().addAll(toolbar, contentArea);
        var scene = new Scene(root, Color.TRANSPARENT);
        setScene(scene);

        initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        root.setMaxSize(600, 400);

    }

    private static class Delta {
        private double x;
        private double y;
    }


}
