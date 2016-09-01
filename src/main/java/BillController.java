import com.sun.org.apache.bcel.internal.generic.NEW;
import com.sun.tools.javac.util.Name;
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



public class BillController implements Initializable {

    //Edit bill data
    @FXML TextField tvCreatorName;
    @FXML DatePicker datePickerCreateDate;
    @FXML Button buttonStartBill;
    @FXML Button buttonCancelBill;


    //Edit product data
    @FXML Pane paneAddPurchase;
    @FXML TextField tvProductName;
    @FXML TextField tvProductCount;
    @FXML TextField tvProductTax;
    @FXML TextField tvProductMargin;
    @FXML TextField tvProductNetto;
    @FXML TextField tvProductPrice;
    @FXML Button buttonAddPurchase;
    @FXML Button buttonDelPurchase;

    //Table view pane
    @FXML TableView tablePurchaseView;
    @FXML TableColumn columnNo;
    @FXML TableColumn columnName;
    @FXML TableColumn columnCount;
    @FXML TableColumn columnNetto;
    @FXML TableColumn columnTax;
    @FXML TableColumn columnNettoAll;
    @FXML TableColumn columnBrutto;
    @FXML TableColumn columnMargin;
    @FXML TableColumn columnBruttoAll;
    @FXML TableColumn columnPrice;
    @FXML TableColumn columnPriceAll;

    //Indicators view pane
    @FXML Pane paneIndPurchase;
    @FXML Label indProductNetto;
    @FXML Label indProductPrice;
    @FXML Label indProductDiff;
    @FXML Label indProductDiffPercent;


    //Info view pane
    //TODO suma itd.

    //File options pane
    @FXML Button buttonSave;

    private Bill bill;

    public BillController(){}

    public BillController(Bill bill) {
        this.bill = bill;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(bill == null) {
            //datepicker initialize to today
            datePickerCreateDate.setValue(LocalDate.now());
        }
        else {
            //initialize all
            datePickerCreateDate.setValue(bill.getDate());
            tvCreatorName.setText(bill.getName());
            tablePurchaseView.getItems().addAll(bill.getList());

        }

        //tvProductCount should take only numbers (int)
        tvProductCount.setTextFormatter(new TextFormatter<Object>(new UnaryOperator<TextFormatter.Change>() {
            @Override
            public TextFormatter.Change apply(TextFormatter.Change change) {
                if(change.getText().matches("[0-9]") || change.getText().matches("")) {
                    return change;
                }else
                return null;
            }
        }));

        //tvProductTax should take only numbers (int)
        tvProductTax.setTextFormatter(new TextFormatter<Object>(new UnaryOperator<TextFormatter.Change>() {
            @Override
            public TextFormatter.Change apply(TextFormatter.Change change) {
                if(change.getText().matches("[0-9]") || change.getText().matches("")) {
                    return change;
                }else
                    return null;
            }
        }));

        //tvProductMargin should take only numbers (int)
        tvProductMargin.setTextFormatter(new TextFormatter<Object>(new UnaryOperator<TextFormatter.Change>() {
            @Override
            public TextFormatter.Change apply(TextFormatter.Change change) {
                if(change.getText().matches("[0-9]") || change.getText().matches("")) {
                    return change;
                }else
                    return null;
            }
        }));

        //tvProductNetto should take only numbers (double)
        tvProductNetto.setTextFormatter(new TextFormatter<Object>(new UnaryOperator<TextFormatter.Change>() {
            @Override
            public TextFormatter.Change apply(TextFormatter.Change change) {
                if(change.getText().matches("[0-9]") || change.getText().matches("") || change.getText().matches(".")){
                    return change;
                }else
                    return null;
            }
        }));

        //tvProductPrice should take only numbers (double)
        tvProductPrice.setTextFormatter(new TextFormatter<Object>(new UnaryOperator<TextFormatter.Change>() {
            @Override
            public TextFormatter.Change apply(TextFormatter.Change change) {
                if(change.getText().matches("[0-9]") || change.getText().matches("") || change.getText().matches(".")){
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

                tvCreatorName.setDisable(true);
                datePickerCreateDate.setDisable(true);
                paneAddPurchase.setDisable(false);
                paneIndPurchase.setDisable(false);
                buttonStartBill.setDisable(true);
                buttonCancelBill.setDisable(false);
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
                String productMarginString = tvProductMargin.getText().trim();
                String productNettoString = tvProductNetto.getText().trim();
                String productPriceString = tvProductPrice.getText().trim();

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
                //productMargin
                if(productMarginString.isEmpty()){
                    tvProductMargin.getStyleClass().add("error");
                    tvProductMargin.setPromptText("puste!");
                    return;
                }
                int productMargin = Integer.parseInt(productMarginString);
                if(!(productTax >= 0 && productTax <= 100)) {
                    tvProductMargin.getStyleClass().add("error");
                    tvProductMargin.setPromptText("zły zakres!");
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

                //productPrice
                double productPrice;
                try {
                    productPrice = Double.parseDouble(productPriceString);
                }
                catch (NumberFormatException e) {
                    tvProductNetto.getStyleClass().add("error");
                    tvProductNetto.setPromptText("co to?");
                    return;
                }

                tvProductName.setText("");
                tvProductCount.setText("");
                tvProductNetto.setText("");
                tvProductPrice.setText("");

                Purchase purchase = new Purchase(productName,productCount,productTax,productMargin,productNetto,productPrice);
                tablePurchaseView.getItems().add(purchase);
            }
        });

        buttonSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Bill bill = new Bill(tvCreatorName.getText(),datePickerCreateDate.getValue(), new ArrayList(tablePurchaseView.getItems()));
                FileDAO.saveToFile(bill);
            }
        });

        columnName.setCellValueFactory(new PropertyValueFactory<Purchase, String>("productName"));
        columnCount.setCellValueFactory(new PropertyValueFactory<Purchase, Integer>("productCount"));
        columnNetto.setCellValueFactory(new PropertyValueFactory<Purchase, Double>("productNetto"));
        columnTax.setCellValueFactory(new PropertyValueFactory<Purchase, Integer>("productTax"));
        columnNettoAll.setCellValueFactory(new PropertyValueFactory<Purchase, Double>("productNettoAll"));
        columnBrutto.setCellValueFactory(new PropertyValueFactory<Purchase, Double>("productBrutto"));
        columnMargin.setCellValueFactory(new PropertyValueFactory<Purchase, Integer>("productMargin"));
        columnBruttoAll.setCellValueFactory(new PropertyValueFactory<Purchase, Double>("productBruttoPrice"));
        columnPrice.setCellValueFactory(new PropertyValueFactory<Purchase, Double>("productPrice"));
        columnPriceAll.setCellValueFactory(new PropertyValueFactory<Purchase, Integer>("productPriceAll"));

    }
}
