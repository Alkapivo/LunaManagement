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
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;
import javafx.scene.control.TableView.TableViewSelectionModel;



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
    @FXML Label indProductBrutto;
    @FXML Label indProductPrice;
    @FXML Label indProductDiff;
    @FXML Label indProductDiffPercent;


    //Info view pane
    @FXML Label sumNetto;
    @FXML Label sumPrice;

    //File options pane
    @FXML Button buttonSave;
    @FXML Button buttonExport;
    @FXML Button buttonPrint;
    @FXML Button buttonImport;

    private Bill bill;

    public BillController(){}

    public BillController(Bill bill) {
        this.bill = bill;
    }

    public void sumTable() {
        //TODO wyswietlanie sumy netto i ceny recznej
        int totalSumNettoSize = tablePurchaseView.getItems().size();
        double totalSumNetto = 0;
        double totalSumPrice = 0;
        String sum;
        for (int i=0; i<totalSumNettoSize; i++) {
            sum = columnNettoAll.getCellObservableValue(i).getValue().toString();
            totalSumNetto += Double.parseDouble(sum);
            sum = columnPriceAll.getCellObservableValue(i).getValue().toString();
            totalSumPrice += Double.parseDouble(sum);
        }
        sumNetto.setText(Double.toString(totalSumNetto)+" zł");
        sumPrice.setText(Double.toString(totalSumPrice)+" zł");
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

        //TODO pasowaloby to jakos uporzadkowac
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
        //TODO bug found - mozna wpisywac litery itd.
        //TODO trzeba porzadnie zrobic aktualizacje wskaznikow, bo sa empty stringi no i nie da sie dodac produktu do listy wtedy
        tvProductNetto.setTextFormatter(new TextFormatter<Object>(new UnaryOperator<TextFormatter.Change>() {
            @Override
            public TextFormatter.Change apply(TextFormatter.Change change) {
                if(change.getText().matches("[0-9]") || change.getText().matches("") || change.getText().matches(".")){
                    /*
                    double valNetto = Double.parseDouble(tvProductNetto.getText()+change.getText());
                    double valTax = Double.parseDouble(tvProductTax.getText());
                    double valMargin = Double.parseDouble(tvProductMargin.getText());
                    double valBrutto = valNetto*(valTax/100.0)+valNetto*(valMargin/100.0)+valNetto;
                    indProductBrutto.setText(Double.toString(valBrutto)+" zł");
                    */
                    //double valNetto = Double.parseDouble(tvProductNetto.getText()+change.getText());
                    return change;
                }else
                    return null;
            }
        }));

        //tvProductPrice should take only numbers (double)
        //TODO bug found - mozna wpisywac litery itd.
        tvProductPrice.setTextFormatter(new TextFormatter<Object>(new UnaryOperator<TextFormatter.Change>() {
            @Override
            public TextFormatter.Change apply(TextFormatter.Change change) {
                if(change.getText().matches("[0-9]") || change.getText().matches("") || change.getText().matches(".")){
                    /*
                    double valPrice = Double.parseDouble(tvProductPrice.getText()+change.getText());
                    double valDiff = valPrice-Double.parseDouble(tvProductNetto.getText());
                    double valDiffPercent = (valDiff/100.0)*valPrice;
                    indProductPrice.setText(Double.toString(valPrice)+" zł");
                    indProductDiff.setText(Double.toString(valDiff)+" zł");
                    indProductDiffPercent.setText(Double.toString(valDiffPercent)+"%");
                    */
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

                sumTable();
            }
        });

        tablePurchaseView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object oldValue, Object newValue) {
                //TODO ma zwracac dokladny obiekt, a nie pojedyncze wartosci z komorki
                if(tablePurchaseView.getSelectionModel().getSelectedItem() != null)
                {
                    TableViewSelectionModel selectionModel = tablePurchaseView.getSelectionModel();
                    ObservableList selectedCells = selectionModel.getSelectedCells();
                    TablePosition tablePosition = (TablePosition) selectedCells.get(0);
                    Object val = tablePosition.getTableColumn();
                    System.out.println("Selected Value" + val);
                }
            }
        });

        //TODO pasowaloby zrobic mozliwosc edycji danych, jesli jakis wiersz jest zaznaczony


        buttonDelPurchase.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //TODO jeśli zaznaczony został obiekt z tablePurchaseView to go usun, jesli nie to ustaw button na disable
            }
        });
        buttonSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //TODO dodaj okienko zapisywania
                Bill bill = new Bill(tvCreatorName.getText(),datePickerCreateDate.getValue(), new ArrayList(tablePurchaseView.getItems()));
                FileDAO.saveToFile(bill);
            }
        });

        //TODO pasowaloby zrobic eksport do pdf tablePurchaseView (i pozostale dane)

        //TODO jak zrobi sie eksport do pdf-a to pasowaloby zrobic mozliwosc drukowania tego pdfa jednym kliknieciem


        columnNo.setCellValueFactory(new Callback<CellDataFeatures<Purchase, Purchase>, ObservableValue<Purchase>>() {
            @Override public ObservableValue<Purchase> call(CellDataFeatures<Purchase, Purchase> p) {
                return new ReadOnlyObjectWrapper((tablePurchaseView.getItems().indexOf(p.getValue()))+1+".");
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
        sumTable();
    }
}
