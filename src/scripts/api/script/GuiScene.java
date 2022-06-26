package scripts.api.script;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToolbar;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;
import scripts.api.classes.GitHub;

import java.util.function.Supplier;

/* Written by IvanEOD 6/25/2022, at 4:07 PM */
@Getter
public class GuiScene extends Stage {

    private VBox root;
    private ToolBar toolbar;
    private JFXButton minimizeButton;
    private JFXButton closeButton;

    public GuiScene() {
        super();
        initStyle(StageStyle.UNDECORATED);


    }

    public void buildContent() {

        setTitle("IvanEOD Scripts");
        setResizable(false);

        root = new VBox();
//        root.setStyle("-fx-background-color: -background-default; -fx-background-radius: 10;" );
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
//        toolbarHbox.setStyle("-fx-background-color: transparent;");


        var titleLogo = new ImageView();
        titleLogo.setFitHeight(21);
        titleLogo.setFitWidth(21);
        titleLogo.setPreserveRatio(true);
        titleLogo.setSmooth(true);
//        titleLogo.setStyle("-fx-text-fill: -text-secondary;");
        Image logo = GitHub.getImage("tribotLogo");
        titleLogo.setImage(logo);

        var titleLabel = new Label("IvanEOD Scripts");
        titleLabel.getStyleClass().add("title");

        var toolbarLeft = new HBox(5, titleLogo, titleLabel);
        toolbarLeft.setPadding(new Insets(0));
        toolbarLeft.setAlignment(Pos.CENTER_LEFT);
        toolbarLeft.setPrefSize(550, 21);
//        toolbarLeft.setStyle("-fx-background-color: transparent;");

        closeButton = new JFXButton("X");
        closeButton.setButtonType(JFXButton.ButtonType.RAISED);
//        closeButton.setStyle("-fx-border-color: -divider-color;" +
//                "    -fx-border-width: 1;\n" +
//                "    -fx-border-radius: 5;\n" +
//                "    -fx-background-radius: 5;" +
//                "    -fx-background-color: -error-color;\n" +
//                "    -fx-text-fill: -error-contrast-text;");
        closeButton.setCancelButton(true);
        closeButton.setPrefSize(15, 15);
        closeButton.setMaxSize(15, 15);
        closeButton.getStyleClass().add("close-button");
        closeButton.setOnAction(e -> Platform.exit());
        closeButton.setAlignment(Pos.CENTER);

        minimizeButton = new JFXButton("_");
        minimizeButton.setButtonType(JFXButton.ButtonType.RAISED);
//        minimizeButton.setStyle("    -fx-border-color: -divider-color;\n" +
//                "    -fx-border-width: 1;\n" +
//                "    -fx-border-radius: 5;\n" +
//                "    -fx-text-fill: -text-secondary;\n" +
//                "    -fx-background-radius: 5;");
        minimizeButton.setPrefSize(15, 15);
        minimizeButton.setMaxSize(15, 15);
        minimizeButton.getStyleClass().add("minimize-button");
        minimizeButton.setOnAction(e -> ((Stage) minimizeButton.getScene().getWindow()).setIconified(true));
        minimizeButton.setAlignment(Pos.CENTER);

//        toolbar.setStyle("    -fx-background-radius: 10 10 0 0;\n" +
//                "    -fx-background-color: -background-paper-darker;\n" +
//                "    -fx-border-width: 0 0 0 0;\n" +
//                "    -fx-border-color: -divider-color;");

        var toolbarRight = new HBox(1, minimizeButton, closeButton);
        toolbarRight.setPrefSize(38, 21);
        toolbarRight.setMaxSize(38, 21);
        toolbarRight.setPadding(new Insets(1));

        toolbarHbox.getChildren().addAll(toolbarLeft, toolbarRight);
        toolbar.getItems().add(toolbarHbox);
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

        var content = new AnchorPane();
        content.setPrefSize(600, 380);
//        content.setStyle("    -fx-border-width: 0 2 2 2;\n" +
//                "    -fx-border-radius: 0 0 10 10;\n" +
//                "    -fx-border-color: -background-paper-darker;");
        content.getStyleClass().add("content-pane");

        var contentArea = new VBox(menubar, content);
        contentArea.setPrefSize(600, 400);
        contentArea.setStyle("-fx-background-color: transparent;");
        //</editor-fold>

        root.getChildren().addAll(toolbar, contentArea);
        var scene = new Scene(root, Color.TRANSPARENT);
        setScene(scene);


    }


//
//<AnchorPane style="-fx-background-color: transparent;" styleClass="background" xmlns="http://javafx.com/javafx/15" xmlns:fx="http://javafx.com/fxml/1" fx:controller="scripts.AccountSetupController">
//   <children>
//      <VBox prefHeight="434.0" prefWidth="600.0" styleClass="main-window">
//         <children>
//            <ToolBar fx:id="mainToolBar" maxHeight="25.0" maxWidth="600.0" minHeight="25.0" prefHeight="25.0" prefWidth="600.0">
//              <items>
//                  <HBox alignment="CENTER_RIGHT" maxHeight="25.0" minHeight="25.0" prefHeight="25.0" prefWidth="588.0" spacing="2.0" styleClass="tool-bar">
//                     <children>
//                        <HBox alignment="CENTER_LEFT" prefHeight="21.0" prefWidth="550.0" spacing="5.0">
//                           <children>
//                              <ImageView fitHeight="21.0" fitWidth="21.0" pickOnBounds="true" preserveRatio="true" />
//                              <Label styleClass="title" text="IvanEOD Scripts" />
//                           </children>
//                        </HBox>
//                        <JFXButton fx:id="minimizeButton" buttonType="RAISED" layoutX="534.0" layoutY="12.0" maxHeight="18.0" maxWidth="18.0" minHeight="18.0" minWidth="18.0" prefHeight="18.0" prefWidth="18.0" styleClass="minimize-button" text="_">
//                           <font>
//                              <Font size="8.0" />
//                           </font>
//                        </JFXButton>
//                        <JFXButton fx:id="closeButton" alignment="CENTER" buttonType="RAISED" cancelButton="true" contentDisplay="CENTER" maxHeight="18.0" maxWidth="18.0" minHeight="18.0" minWidth="18.0" prefHeight="18.0" prefWidth="18.0" styleClass="close-button" text="X" textAlignment="CENTER">
//                           <font>
//                              <Font name="System Bold" size="8.0" />
//                           </font>
//                        </JFXButton>
//                     </children>
//                     <padding>
//                        <Insets bottom="2.0" top="2.0" />
//                     </padding>
//                  </HBox>
//              </items>
//            </ToolBar>
//            <VBox maxWidth="600.0" minWidth="600.0" prefHeight="408.0" prefWidth="600.0" style="-fx-background-radius: 10;">
//              <children>
//                <MenuBar maxHeight="25.0" prefHeight="10.0" prefWidth="600.0" VBox.vgrow="NEVER">
//                  <menus>
//                    <Menu mnemonicParsing="false" text="File">
//                      <items>
//                        <MenuItem mnemonicParsing="false" text="Preferences…" />
//                        <SeparatorMenuItem mnemonicParsing="false" />
//                        <MenuItem mnemonicParsing="false" text="Quit" />
//                      </items>
//                    </Menu>
//                    <Menu mnemonicParsing="false" text="Edit">
//                      <items>
//                        <MenuItem mnemonicParsing="false" text="Copy" />
//                        <MenuItem mnemonicParsing="false" text="Paste" />
//                      </items>
//                    </Menu>
//                    <Menu mnemonicParsing="false" text="Help">
//                      <items>
//                        <MenuItem mnemonicParsing="false" text="About MyHelloApp" />
//                      </items>
//                    </Menu>
//                  </menus>
//                </MenuBar>
//                <AnchorPane fx:id="contentPane" disable="true" maxHeight="-1.0" maxWidth="-1.0" prefHeight="348.0" prefWidth="600.0" styleClass="content-pane" VBox.vgrow="ALWAYS" />
//              </children>
//            </VBox>
//         </children>
//      </VBox>
//   </children>
//</AnchorPane>


    private static <NodeType extends Region> NodeType setup(Supplier<NodeType> nodeSupplier, double width, double height) {
        var node = nodeSupplier.get();
        node.setMinSize(width, height);
        node.setPrefSize(width, height);
        node.setMaxSize(width, height);
        return node;
    }

    private static <NodeType extends Region> NodeType setup(Supplier<NodeType> nodeSupplier, String className, double width, double height) {
        var node = nodeSupplier.get();
        node.getStyleClass().add(className);
        node.setMinSize(width, height);
        node.setPrefSize(width, height);
        node.setMaxSize(width, height);
        return node;
    }


}
