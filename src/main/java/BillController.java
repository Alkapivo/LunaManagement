import javafx.fxml.FXML;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;
import java.awt.print.PrinterException;
import org.apache.pdfbox.pdmodel.PDDocument;

import javax.print.PrintService;


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
        sumNetto.setText(String.format("%.2f", totalSumNetto)+" zł");
        sumPrice.setText(String.format("%.2f", totalSumPrice)+" zł");
        //sumNetto.setText(Double.toString(totalSumNetto)+" zł");
        //sumPrice.setText(Double.toString(totalSumPrice)+" zł");
    }

    public void updateIndBrutto() {
        String indTax = tvProductTax.getText();
        String indMargin = tvProductMargin.getText();
        String indNetto = tvProductNetto.getText();
        double indBrutto = 0;

        if (indTax.isEmpty())
            indTax = "0";
        if (indMargin.isEmpty())
            indMargin = "0";
        if (indNetto.isEmpty())
            indNetto = "0";

        indBrutto = Double.parseDouble(indNetto)*(Double.parseDouble(indTax)/100.0)+Double.parseDouble(indNetto)*(Double.parseDouble(indMargin)/100.0)+Double.parseDouble(indNetto);
        indProductBrutto.setText(String.format("%.2f", indBrutto));

        updateIndDiff();
    }

    public void typeTextFieldInt(TextField tv) {
        tv.setTextFormatter(new TextFormatter<Object>(new UnaryOperator<TextFormatter.Change>() {
            @Override
            public TextFormatter.Change apply(TextFormatter.Change change) {
                if(change.getText().matches("[0-9]") || change.getText().matches("")) {
                    return change;
                }else
                    return null;
            }
        }));

    }

    public void typeTextFieldMoney(TextField tv) {
        tv.setTextFormatter(new TextFormatter<Object>(new UnaryOperator<TextFormatter.Change>() {
            @Override
            public TextFormatter.Change apply(TextFormatter.Change change) {
                String[] temp = tv.getText().split("\\.");

                if (change.getText().equals(",")) {
                    change.setText(".");
                }
                if(change.getText().matches("[0-9]") || change.getText().matches("") || change.getText().matches("\\.")) {
                    if (tv.getText().contains(".") && change.getText().equals(".")) {
                        return null;
                    }
                    if (temp.length > 1 && temp[1].length() >= 2 && !change.getText().matches("")) {
                        return null;
                    }
                    return change;
                }else
                    return null;
            }
        }));

    }

    public void updateIndDiff() {
        String indBrutto = indProductBrutto.getText().replace(',', '.');
        String indPrice = indProductPrice.getText().replace(',', '.');
        if (indBrutto.isEmpty())
            indBrutto = "0";
        if (indPrice.isEmpty())
            indPrice = "0";

        double indDiff;
        double indDiffP;
        try {
            indDiff = Double.parseDouble(indPrice)-Double.parseDouble(indBrutto);
        }
        catch (NumberFormatException e) {
            indDiff = 0;
        }
        try {
            if (Double.parseDouble(indPrice) != 0)
                indDiffP = (indDiff/Double.parseDouble(indPrice))*100;
            else
                indDiffP = 0;
        }
        catch (NumberFormatException e) {
            indDiffP = 0;
        }

        indProductDiff.setText(String.format("%.2f",indDiff));
        indProductDiffPercent.setText(String.format("%.0f",indDiffP)+"%");
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

        typeTextFieldInt(tvProductCount);
        typeTextFieldInt(tvProductTax);
        typeTextFieldInt(tvProductMargin);
        typeTextFieldMoney(tvProductNetto);
        typeTextFieldMoney(tvProductPrice);

        tvProductNetto.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) { updateIndBrutto(); }
        });

        tvProductTax.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) { updateIndBrutto(); }
        });

        tvProductMargin.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) { updateIndBrutto(); }
        });

        indProductBrutto.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) { updateIndBrutto(); }
        });

        tvProductPrice.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String indPrice = tvProductPrice.getText().trim();
                if (indPrice.isEmpty())
                    indPrice = "0";
                double indPriceD = Double.parseDouble(indPrice);
                indProductPrice.setText(String.format("%.2f", indPriceD));
                updateIndDiff();
            }
        });

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
                    tvProductPrice.getStyleClass().add("error");
                    tvProductPrice.setPromptText("co to??");
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

        buttonDelPurchase.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(tablePurchaseView.getSelectionModel().getSelectedItem() != null) {
                    int selectedRow = tablePurchaseView.getSelectionModel().getSelectedIndex();
                    tablePurchaseView.getItems().remove(selectedRow);
                    sumTable();
                }

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

        buttonExport.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                PDFCreator.createPDF(tablePurchaseView,
                                        tvCreatorName.getText(),
                                        datePickerCreateDate.getValue().toString(),
                                        sumNetto.getText(),
                                        sumPrice.getText());
            }
        });

        buttonPrint.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                PrintService service = PDFPrint.choosePrinter();
                PDFPrint.printPDF("test2.pdf", service);
            }
        });



        tablePurchaseView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object oldValue, Object newValue) {
                if(tablePurchaseView.getSelectionModel().getSelectedItem() != null) {
                    int selectedRow = tablePurchaseView.getSelectionModel().getSelectedIndex();
                }
            }
        });

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
