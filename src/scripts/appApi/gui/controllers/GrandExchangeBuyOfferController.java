package scripts.appApi.gui.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.IntegerValidator;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.text.Text;
import scripts.appApi.concurrency.DebounceManager;
import scripts.appApi.concurrency.Debouncer;
import scripts.appApi.script.ScriptGuiController;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/* Written by IvanEOD 6/26/2022, at 2:35 PM */
public class GrandExchangeBuyOfferController extends ScriptGuiController {

    private final SimpleIntegerProperty pricePer = new SimpleIntegerProperty(0);
    private final SimpleIntegerProperty quantity = new SimpleIntegerProperty(0);
    private final SimpleIntegerProperty totalPrice = new SimpleIntegerProperty(0);
    private final SimpleIntegerProperty itemMarketPrice = new SimpleIntegerProperty(0);
    private final SimpleIntegerProperty itemId = new SimpleIntegerProperty(0);
    private final SimpleStringProperty itemName = new SimpleStringProperty("");
    private final SimpleStringProperty itemDescription = new SimpleStringProperty("");
    private final SimpleStringProperty customPriceInput = new SimpleStringProperty("");
    private final SimpleStringProperty customQuantityInput = new SimpleStringProperty("");


    @FXML
    private JFXButton plus100Button;
    @FXML
    private JFXTextField totalPriceField;
    @FXML
    private JFXButton plus10Button;
    @FXML
    private JFXButton priceSubtractButton;
    @FXML
    private JFXButton customPriceButton;
    @FXML
    private JFXButton resetPriceButton;
    @FXML
    private JFXButton confirmButton;
    @FXML
    private JFXTextField itemNameInput;
    @FXML
    private JFXTextField itemIdInput;
    @FXML
    private JFXButton customQuantityButton;
    @FXML
    private JFXButton cancelButton;
    @FXML
    private JFXButton minus5PercentButton;
    @FXML
    private JFXTextField priceField;
    @FXML
    private JFXButton quantitySubtractButton;
    @FXML
    private JFXButton plus5PercentButton;
    @FXML
    private JFXButton plus1Button;
    @FXML
    private Text itemMarketPriceText;
    @FXML
    private JFXButton plus1kButton;
    @FXML
    private JFXTextField quantityField;
    @FXML
    private JFXButton quantityAddButton;
    @FXML
    private JFXButton priceAddButton;

    @Override
    public void setupComponents() {

        //<editor-fold desc="Quantity Input Connections">
        quantityAddButton.setOnAction(event -> quantity.add(1));
        quantitySubtractButton.setOnAction(event -> quantity.subtract(1));
        plus1Button.setOnAction(event -> quantity.add(1));
        plus10Button.setOnAction(event -> quantity.add(10));
        plus100Button.setOnAction(event -> quantity.add(100));
        plus1kButton.setOnAction(event -> quantity.add(1000));
        quantity.addListener((observable, oldValue, newValue) -> {
            if (quantity.asString().isNotEqualTo(quantityField.getText()).get()) {
                quantityField.setText(newValue.toString());
                updateTotalPrice();
            }
        });
        setupInputField(quantityField);
        //</editor-fold>

        //<editor-fold desc="Price Input Connections">
        priceAddButton.setOnAction(event -> pricePer.add(1));
        priceSubtractButton.setOnAction(event -> pricePer.subtract(1));
        plus5PercentButton.setOnAction(event -> pricePer.multiply(1.05));
        minus5PercentButton.setOnAction(event -> pricePer.multiply(0.95));
        resetPriceButton.setOnAction(event -> pricePer.set(itemMarketPrice.get()));
        pricePer.addListener((observable, oldValue, newValue) -> {
            if (pricePer.asString().isNotEqualTo(priceField.getText()).get()) {
                priceField.setText(newValue.toString());
                updateTotalPrice();
            }
        });
        setupInputField(priceField);
        //</editor-fold>

        //<editor-fold desc="Text Update Connections">
        itemMarketPrice.addListener((observable, oldValue, newValue) -> {
            if (itemMarketPrice.asString().isNotEqualTo(priceField.getText()).get()) {
                priceField.setText(newValue.toString());
                updateTotalPrice();
            }
        });
        itemNameInput.textProperty().addListener((observable, oldValue, newValue) -> onItemNameChanged());
        itemIdInput.textProperty().addListener((observable, oldValue, newValue) -> onItemIdChanged());

        itemMarketPriceText.textProperty().bind(itemMarketPrice.asString());

        //</editor-fold>



    }

    private void updateTotalPrice() {
        totalPrice.set(quantity.get() * pricePer.get());
    }

    private void setupInputField(JFXTextField textField) {
        var isQuantity = textField == quantityField;
        var field = isQuantity ? quantityField : priceField;
        var value = isQuantity ? quantity : pricePer;

        textField.setEditable(false);
        textField.setValidators(new IntegerValidator());
        textField.setLabelFloat(true);
        textField.setAlignment(Pos.CENTER);
        textField.setFocusTraversable(false);

        textField.focusedProperty().addListener((observable, wasFocused, isFocused) -> {
            if (!isFocused) {
                field.setEditable(false);
                field.setFocusTraversable(false);
                if (value.get() != Integer.parseInt(field.getText())) value.set(Integer.parseInt(field.getText()));
                updateTotalPrice();
            }
        });

        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            var intValue = Integer.parseInt(newValue);
            if (value.get() != intValue) {
                value.set(intValue);
                updateTotalPrice();
            }
        });

    }

    private void onItemNameChanged() {
        DebounceManager.call(itemNameDebouncer, 500, TimeUnit.MILLISECONDS);
    }

    private void onItemIdChanged() {
        DebounceManager.call(itemIdDebouncer, 500, TimeUnit.MILLISECONDS);
    }

    private final Debouncer itemIdDebouncer = () -> {
        var idInputValue= itemIdInput.getText();
        int id;
        if (idInputValue.isEmpty()) return;
        else id = Integer.parseInt(idInputValue);
        if (itemId.get() != id) itemId.set(id);
    };

    private final Debouncer itemNameDebouncer = () -> {
        if (!Objects.equals(itemName.get(), itemNameInput.getText())) itemName.set(itemNameInput.getText());
    };

}
