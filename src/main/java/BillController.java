import com.sun.org.apache.bcel.internal.generic.NEW;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
public class BillController implements Initializable {

    @FXML
    TextField tvCreatorName;

    @FXML
    DatePicker datePickerCreateDate;

    @FXML
    Button buttonStartBill;

    @FXML
    Button buttonCancelBill;

    @FXML
    Button buttonSave;

    @FXML
    Pane paneAddPurchase;

    @FXML
    TextField tvProductName;

    @FXML
    TextField tvProductCount;

    @FXML
    TextField tvProductTax;

    @FXML
    TextField tvProductNetto;

    @FXML
    Button buttonAddPurchase;

    @FXML
    TableColumn columnName;

    @FXML
    TableColumn columnCount;

    @FXML
    TableColumn columnTax;

    @FXML
    TableColumn columnNetto;

    @FXML
    TableColumn columnBrutto;

    @FXML
    TableView tablePurchasesView;

    private Bill bill;

    public BillController(){}

    public BillController(Bill bill) {
        this.bill = bill;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(bill==null){
            //datepicker initialize to today
            datePickerCreateDate.setValue(LocalDate.now());
        }else{
            //initialize all
            datePickerCreateDate.setValue(bill.getDate());
            tvCreatorName.setText(bill.getName());
            tablePurchasesView.getItems().addAll(bill.getList());

        }


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

        //tvProductTax should take only numbers
        tvProductTax.setTextFormatter(new TextFormatter<Object>(new UnaryOperator<TextFormatter.Change>() {
            @Override
            public TextFormatter.Change apply(TextFormatter.Change change) {
                if(change.getText().matches("[0-9]") || change.getText().matches("") || change.getText().matches("\b")){
                    return change;
                }else
                    return null;
            }
        }));

        //tvProductTax should take only numbers
        tvProductNetto.setTextFormatter(new TextFormatter<Object>(new UnaryOperator<TextFormatter.Change>() {
            @Override
            public TextFormatter.Change apply(TextFormatter.Change change) {
                if(change.getText().matches("[0-9]") || change.getText().matches("") || change.getText().matches("\b") || change.getText().matches(".")){
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
                tvProductTax.getStyleClass().remove("error");
                tvProductTax.setPromptText("");
                tvProductNetto.getStyleClass().remove("error");
                tvProductNetto.setPromptText("");

                String productName = tvProductName.getText().trim();
                String productCountString = tvProductCount.getText().trim();
                String productTaxString = tvProductTax.getText().trim();
                String productNettoString = tvProductNetto.getText().trim();

                //productName
                //everything is ok :)

                //productCount
                if(productCountString.isEmpty()){
                    tvProductCount.getStyleClass().add("error");
                    tvProductCount.setPromptText("puste!");
                    return;
                }
                int productCount = Integer.parseInt(productCountString);

                //productTax
                if(productTaxString.isEmpty()){
                    tvProductTax.getStyleClass().add("error");
                    tvProductTax.setPromptText("puste!");
                    return;
                }
                int productTax = Integer.parseInt(productTaxString);
                if(!(productTax >= 0 && productTax <= 100)) {
                    tvProductTax.getStyleClass().add("error");
                    tvProductTax.setPromptText("zły zakres!");
                    return;
                }

                //productNetto
                double productNetto;
                try {
                    productNetto = Double.parseDouble(productNettoString);
                }
                catch (NumberFormatException e) {
                    tvProductNetto.getStyleClass().add("error");
                    tvProductNetto.setPromptText("co to?");
                    return;
                }

                tvProductName.setText("");
                tvProductCount.setText("");

                Purchase purchase = new Purchase(productName,productCount,productTax,productNetto);
                tablePurchasesView.getItems().add(purchase);
            }
        });

        buttonSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Bill bill = new Bill(tvCreatorName.getText(),datePickerCreateDate.getValue(), tablePurchasesView.getItems());
                FileDAO.saveToFile(bill);
            }
        });

        columnName.setCellValueFactory(new PropertyValueFactory<Purchase, String>("productName"));
        columnCount.setCellValueFactory(new PropertyValueFactory<Purchase, Integer>("productCount"));
        columnTax.setCellValueFactory(new PropertyValueFactory<Purchase, Integer>("productTax"));
        columnNetto.setCellValueFactory(new PropertyValueFactory<Purchase, Double>("productNetto"));
        columnBrutto.setCellValueFactory(new PropertyValueFactory<Purchase, Double>("productBrutto"));
    }
}
