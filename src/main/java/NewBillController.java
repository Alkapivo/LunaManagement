import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

/**
 * Created by Krolis on 2016-08-26.
 */
public class NewBillController implements Initializable {

    @FXML
    TextField tvCreatorName;

    @FXML
    DatePicker datePickerCreateDate;

    @FXML
    Button buttonStartBill;

    @FXML
    Button buttonCancelBill;

    @FXML
    Pane paneAddPurchase;

    @FXML
    TextField tvProductName;

    @FXML
    TextField tvProductCount;

    @FXML
    Button buttonAddPurchase;

    @FXML
    ListView<Purchase> listViewPurchases;

    List<Purchase> purchaseList = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //datepicker initialize to today
        datePickerCreateDate.setValue(LocalDate.now());

        //tvProductCount should take only numbers
        tvProductCount.setTextFormatter(new TextFormatter<Object>(new UnaryOperator<TextFormatter.Change>() {
            @Override
            public TextFormatter.Change apply(TextFormatter.Change change) {
                if(change.getText().matches("[0-9]") || change.getText().matches("") || change.getText().matches("\b")){
                    return change;
                }else
                return null;
            }
        }));

        buttonStartBill.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String creatorName = tvCreatorName.getText();
                LocalDate date = datePickerCreateDate.getValue();
                System.out.println(creatorName + date.toString());

                tvCreatorName.setDisable(true);
                datePickerCreateDate.setDisable(true);
                buttonStartBill.setDisable(true);
                buttonCancelBill.setDisable(false);
                paneAddPurchase.setDisable(false);
            }
        });

        buttonAddPurchase.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                tvProductCount.getStyleClass().remove("error");
                tvProductCount.setPromptText("");
                String productName = tvProductName.getText().trim();
                String productCountString = tvProductCount.getText().trim();
                if(productCountString.isEmpty()){
                    tvProductCount.getStyleClass().add("error");
                    tvProductCount.setPromptText("podaj ilość!");
                    return;
                }
                int productCount = Integer.parseInt(productCountString);


                System.out.println("New row in bill: " + productName + " count: " + productCount);

                tvProductName.setText("");
                tvProductCount.setText("");

                Purchase purchase = new Purchase(productName,productCount);
                purchaseList.add(purchase);
                listViewPurchases.setItems(FXCollections.observableList(purchaseList));
            }
        });
    }
}
