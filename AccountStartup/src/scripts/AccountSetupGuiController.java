package scripts;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.tribot.script.sdk.Log;
import org.tribot.script.sdk.Options;
import scripts.api.classes.FileHelper;
import scripts.api.classes.GitHub;
import scripts.api.script.ScriptGuiController;
import scripts.classes.AccountSetupOptions;

import java.net.URL;
import java.util.ResourceBundle;

/* Written by IvanEOD 6/24/2022, at 6:57 AM */
public class AccountSetupGuiController extends ScriptGuiController {

    //<editor-fold defaultstate="collapsed" desc="FXML Fields">
    @FXML
    private JFXCheckBox muteSoundEffects;
    @FXML
    private JFXCheckBox waitForTradeFromMule;
    @FXML
    private TextField addNameInput;
    @FXML
    private TextField displayName;
    @FXML
    private JFXCheckBox addRandomNumbers;
    @FXML
    private JFXCheckBox onlyTradeListedAccounts;
    @FXML
    private TextField maxAmountInput;
    @FXML
    private JFXCheckBox taskAdjustSettings;
    @FXML
    private JFXCheckBox hideRoofs;
    @FXML
    private ListView<ListedAccount> namesList;
    @FXML
    private JFXCheckBox useLeetSpeak;
    @FXML
    private ImageView logo;
    @FXML
    private JFXRadioButton hardcoreIronAccount;
    @FXML
    private JFXCheckBox adjustScreenDisplay;
    @FXML
    private JFXCheckBox usePriceModifier;
    @FXML
    private JFXCheckBox removeTradeDelay;
    @FXML
    private JFXCheckBox muteMusic;
    @FXML
    private JFXButton startButton;
    @FXML
    private ToggleGroup accountType;
    @FXML
    private JFXComboBox<Options.ResizableType> screenDisplayChoice;
    @FXML
    private JFXCheckBox logoutWhenFinished;
    @FXML
    private JFXRadioButton normalAccount;
    @FXML
    private JFXCheckBox taskPurchaseBond;
    @FXML
    private JFXRadioButton ultimateIronAccount;
    @FXML
    private JFXButton addNameButton;
    @FXML
    private JFXCheckBox muteAreaSound;
    @FXML
    private JFXCheckBox taskDoTutorial;
    @FXML
    private JFXCheckBox splitPrivateChat;
    @FXML
    private JFXCheckBox tryToSellItems;
    @FXML
    private JFXCheckBox logoutIfCannotAfford;
    @FXML
    private JFXRadioButton ironAccount;
    @FXML
    private JFXCheckBox disableProfanityFilter;
    @FXML
    private TextField optionsNameInput;
    @FXML
    private JFXButton optionsLoadButton;
    @FXML
    private JFXComboBox<String> optionsSelector;
    @FXML
    private JFXButton optionsSaveButton;
    //</editor-fold>

    private final SimpleObjectProperty<ListedAccount> selectedListedAccount = new SimpleObjectProperty<>(null);

    private AccountSetupOptions accountSetupOptions;

    private class ListedAccount extends HBox {

        private String name;
        private Label nameLabel;
        private JFXButton removeButton;

        public ListedAccount(String name) {
            super(2);
            this.name = name;
            nameLabel = new Label(name);
            nameLabel.setStyle("-fx-font-size: 8px;");
            nameLabel.setMaxSize(100, 10);
            removeButton = new JFXButton("-");
            removeButton.setMaxSize(10, 10);
            removeButton.setStyle("-fx-background-color: #FF0033; -fx-font-size: 8px;");
            removeButton.setOnAction(event -> onRemoveNameButtonPressed(this));
            setStyle("-fx-alignment: center-left;");
            getChildren().addAll(nameLabel, removeButton);
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        var tribotLogo = GitHub.getImage("tribotLogo");
        if (tribotLogo != null) logo.setImage(tribotLogo);

        accountSetupOptions = new AccountSetupOptions().load("lastRun");
//        startButton.setOnAction(event -> this.onStartButtonPressed());
//        addNameButton.setOnAction(event -> this.onAddNameButtonPressed());
//
//        optionsLoadButton.setOnAction(event -> this.onOptionsLoadButtonPressed());
//        optionsSaveButton.setOnAction(event -> this.onOptionsSaveButtonPressed());
//
//        var savedOptions = accountSetupOptions.getSaveFileNames();
//
//        optionsSelector.getItems().add("default");
//        if (!savedOptions.isEmpty()) optionsSelector.getItems().addAll(savedOptions);

    }

    private void onOptionsLoadButtonPressed() {
        var selected = optionsSelector.getSelectionModel().getSelectedItem();
        if (selected == null) selected = "default";
        accountSetupOptions.load(selected);
        updateOptionsToGui();
    }

    private void onOptionsSaveButtonPressed() {
        if (optionsNameInput.getText().isEmpty()) {
            Log.warn("You must enter a name for the options file.");
            return;
        }
        accountSetupOptions.save(optionsNameInput.getText());
        optionsSelector.getItems().add(optionsNameInput.getText());
    }

    private void onRemoveNameButtonPressed(ListedAccount listedAccount) {
        namesList.getChildrenUnmodifiable().remove(listedAccount);
    }

    private void onAddNameButtonPressed() {
        if (addNameInput.getText().isEmpty()) return;
        var inputName = addNameInput.getText();
        if (namesList.getItems().stream().anyMatch(listedAccount -> listedAccount.name.equals(inputName))) return;
        namesList.getItems().add(new ListedAccount(inputName));
        addNameInput.setText("");
    }

    private void onStartButtonPressed() {
        Log.info("Pressed Start!");
    }


    private void updateOptionsToGui() {
        displayName.setText(accountSetupOptions.getDisplayName());
        taskDoTutorial.setSelected(accountSetupOptions.isTaskDoTutorial());
        taskAdjustSettings.setSelected(accountSetupOptions.isTaskAdjustSettings());
        taskPurchaseBond.setSelected(accountSetupOptions.isTaskBuyBond());

        addRandomNumbers.setSelected(accountSetupOptions.isAddRandomNumbers());
        useLeetSpeak.setSelected(accountSetupOptions.isReplaceLettersWithLeetSpeak());
        switch (accountSetupOptions.getAccountType()) {
            case IRONMAN:
                ironAccount.setSelected(true);
                break;
            case HARDCORE_IRONMAN:
                hardcoreIronAccount.setSelected(true);
                break;
            case ULTIMATE_IRONMAN:
                ultimateIronAccount.setSelected(true);
                break;
            default:
                normalAccount.setSelected(true);
                break;
        }

        muteMusic.setSelected(accountSetupOptions.isMuteMusic());
        muteAreaSound.setSelected(accountSetupOptions.isMuteAmbientSound());
        muteSoundEffects.setSelected(accountSetupOptions.isMuteSoundEffects());
        hideRoofs.setSelected(accountSetupOptions.isHideRoofs());
        screenDisplayChoice.setValue(accountSetupOptions.getResizableType());
        splitPrivateChat.setSelected(accountSetupOptions.isSplitPrivateChat());
        disableProfanityFilter.setSelected(accountSetupOptions.isDisableProfanityFilter());
        adjustScreenDisplay.setSelected(accountSetupOptions.isAdjustScreenDisplay());
        removeTradeDelay.setSelected(accountSetupOptions.isRemoveAcceptTradeDelay());

        logoutWhenFinished.setSelected(accountSetupOptions.isLogoutWhenFinished());
        maxAmountInput.setText(String.valueOf(accountSetupOptions.getMaxAmount()));
        waitForTradeFromMule.setSelected(accountSetupOptions.isWaitForTradeFromMule());
        onlyTradeListedAccounts.setSelected(accountSetupOptions.isOnlyTradeListedAccounts());
        logoutIfCannotAfford.setSelected(accountSetupOptions.isLogoutIfCannotAfford());
        tryToSellItems.setSelected(accountSetupOptions.isSellItemsForBond());
        usePriceModifier.setSelected(accountSetupOptions.isUsePriceModifier());
        addNameInput.setText("");

        namesList.getItems().clear();
        namesList.getItems().addAll(accountSetupOptions.getTrustedAccounts()
                .stream()
                .map(ListedAccount::new)
                .toArray(ListedAccount[]::new));

    }

    private void setupListeners() {


    }


}
