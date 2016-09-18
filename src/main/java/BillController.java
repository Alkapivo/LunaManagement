import javafx.fxml.FXML;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.stage.Stage;
import javafx.util.Callback;

import javax.print.PrintService;

import static java.lang.StrictMath.floor;


public class BillController implements Initializable {

    private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");
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
    @FXML TableColumn<Purchase, String> columnName;
    @FXML TableColumn<Purchase, Integer> columnCount;
    @FXML TableColumn<Purchase, Double> columnNetto;
    @FXML TableColumn<Purchase, Integer> columnTax;
    @FXML TableColumn<Purchase, Double> columnNettoAll;
    @FXML TableColumn<Purchase, Double> columnBrutto;
    @FXML TableColumn<Purchase, Integer> columnMargin;
    @FXML TableColumn<Purchase, Double> columnBruttoAll;
    @FXML TableColumn<Purchase, Double> columnPrice;
    @FXML TableColumn<Purchase, Double> columnPriceAll;

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
    @FXML Button buttonSaveFTP;
    @FXML Button buttonSave;
    @FXML Button buttonExport;
    @FXML Button buttonPrint;
    @FXML Button buttonMenu;

    @FXML Pane paneTableView;

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
    }

    public void updateIndBrutto() {
        String indTax = tvProductTax.getText();
        String indMargin = tvProductMargin.getText();
        String indNetto = tvProductNetto.getText();

        if (indTax.isEmpty())       {indTax = "0";}
        if (indMargin.isEmpty())    {indMargin = "0";}
        if (indNetto.isEmpty())     {indNetto = "0";}

        double indTaxD = Double.parseDouble(indTax)/100.0;
        double indMarginD = Double.parseDouble(indMargin)/100.0;
        double indNettoD = Double.parseDouble(indNetto);
        double indBrutto = 0;

        indBrutto = ((indNettoD+indNettoD*indTaxD)*indMarginD+(indNettoD+indNettoD*indTaxD));
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
        Settings.createSettings();
        Settings.loadSettings();
        RecentBillsList.createList();
        RecentBillsList.loadList();

        if(bill == null) {
            datePickerCreateDate.setValue(LocalDate.now());
        }
        else {
            datePickerCreateDate.setValue(bill.getDate());
            tvCreatorName.setText(bill.getName());
            tablePurchaseView.getItems().addAll(bill.getList());
        }

        typeTextFieldInt(tvProductCount);
        typeTextFieldInt(tvProductTax);
        typeTextFieldInt(tvProductMargin);
        typeTextFieldMoney(tvProductNetto);
        typeTextFieldMoney(tvProductPrice);

        tvProductTax.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) { updateIndBrutto(); }
        });

        tvProductMargin.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) { updateIndBrutto(); }
        });

        tvProductNetto.textProperty().addListener(new ChangeListener<String>() {
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

        indProductBrutto.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) { updateIndBrutto(); }
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
                buttonSaveFTP.setDisable(false);
                buttonSave.setDisable(false);
                buttonExport.setDisable(false);
                buttonPrint.setDisable(false);
            }
        });

        buttonCancelBill.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                tvCreatorName.setDisable(false);
                datePickerCreateDate.setDisable(false);
                paneAddPurchase.setDisable(true);
                paneIndPurchase.setDisable(true);
                buttonStartBill.setDisable(false);
                buttonCancelBill.setDisable(true);
                buttonSave.setDisable(true);
                buttonSave.setDisable(true);
                buttonExport.setDisable(true);
                buttonPrint.setDisable(true);
            }
        });

        buttonAddPurchase.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String productName = tvProductName.getText().trim();
                String productCountString = tvProductCount.getText().trim();
                String productTaxString = tvProductTax.getText().trim();
                String productMarginString = tvProductMargin.getText().trim();
                String productNettoString = tvProductNetto.getText().trim();
                String productPriceString = tvProductPrice.getText().trim();

                if (!ValidatePurchase.validateName(productName))            {return;}
                if (!ValidatePurchase.validateCount(productCountString))    {return;}
                if (!ValidatePurchase.validatePercent(productTaxString))    {return;}
                if (!ValidatePurchase.validatePercent(productMarginString)) {return;}
                if (!ValidatePurchase.validatePrice(productNettoString))    {return;}
                if (!ValidatePurchase.validatePrice(productPriceString))    {return;}

                int productCount = Integer.parseInt(productCountString);
                int productTax = Integer.parseInt(productTaxString);
                int productMargin = Integer.parseInt(productMarginString);
                double productNetto = Double.parseDouble(productNettoString);
                double productPrice = Double.parseDouble(productPriceString);

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

        buttonSaveFTP.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FTP.uploadFile();
            }
        });

        buttonSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File fileToSave = OpenDialog.saveFile("Zapisz fakturę",Settings.getBillDirectory(Settings.getHomeDirectory(),datePickerCreateDate.getValue().getYear(),datePickerCreateDate.getValue().getMonth()),".lmb", tvCreatorName.getText());
                Settings.setRecentDirectory(fileToSave);
                Bill bill = new Bill(tvCreatorName.getText(),datePickerCreateDate.getValue(), new ArrayList(tablePurchaseView.getItems()));
                FileDAO.saveToFile(bill, fileToSave);
                RecentBillsList.addBill(fileToSave);
                RecentBillsList.saveList();
            }
        });

        buttonExport.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File fileToSave = OpenDialog.saveFile("Eksportuj fakturę",Settings.getRecentDirectory(),".pdf", tvCreatorName.getText());
                Settings.setRecentDirectory(fileToSave);
                PDFCreator.createPDF(tablePurchaseView,tvCreatorName.getText(),datePickerCreateDate.getValue().toString(),sumNetto.getText(),sumPrice.getText(),fileToSave);
            }
        });

        buttonPrint.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                PrintService service = PDFPrint.choosePrinter();
                File printFile = new File("temp.pdf");
                PDFCreator.createPDF(tablePurchaseView,tvCreatorName.getText(),datePickerCreateDate.getValue().toString(),sumNetto.getText(),sumPrice.getText(),printFile);
                PDFPrint.printPDF(printFile, service);
                try {
                    printFile.delete();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        buttonMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Ostrzeżenie");
                alert.setHeaderText("Powrót do ekranu startowego wiąże się z utratą niezapisanych danych.");
                alert.setContentText("Czy na pewno chcesz wrócić?");

                ButtonType buttonTypeOne = new ButtonType("Tak");
                ButtonType buttonTypeTwo = new ButtonType("Nie");

                alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == buttonTypeOne) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("home_layout.fxml"));
                        loader.setController(new HomeController());
                        Parent root = loader.load();
                        Scene scene = new Scene(root, 1114, 640);
                        Stage stage = (Stage) buttonMenu.getScene().getWindow();
                        stage.setScene(scene);
                        stage.setResizable(false);
                        stage.setTitle("Luna Management");
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        paneTableView.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                double prefHeight = paneTableView.getScene().getHeight();
                paneTableView.setPrefHeight(prefHeight);
                tablePurchaseView.setPrefHeight(prefHeight-240);
            }
        });

        paneTableView.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                double prefWidth = paneTableView.getScene().getWidth();
                double tableWidth = floor((prefWidth-1114)/11.0);
                paneTableView.setPrefWidth(prefWidth);
                tablePurchaseView.setPrefWidth(prefWidth-50);

                columnNo.setPrefWidth(48+tableWidth);
                columnName.setPrefWidth(192+tableWidth);
                columnCount.setPrefWidth(48+tableWidth);
                columnNetto.setPrefWidth(90+tableWidth);
                columnTax.setPrefWidth(96+tableWidth);
                columnNettoAll.setPrefWidth(96+tableWidth);
                columnBrutto.setPrefWidth(96+tableWidth);
                columnMargin.setPrefWidth(108+tableWidth);
                columnBruttoAll.setPrefWidth(96+tableWidth);
                columnPrice.setPrefWidth(96+tableWidth);
                columnPriceAll.setPrefWidth(97+tableWidth);

                buttonSave.setLayoutX(prefWidth-458);
                buttonExport.setLayoutX(prefWidth-350);
                buttonPrint.setLayoutX(prefWidth-242);
                buttonMenu.setLayoutX(prefWidth-134);
            }
        });

        tablePurchaseView.setRowFactory(tv -> {
            TableRow<Purchase> row = new TableRow<>();

            row.setOnDragDetected(event -> {
                if (! row.isEmpty()) {
                    Integer index = row.getIndex();
                    Dragboard db = row.startDragAndDrop(TransferMode.MOVE);
                    db.setDragView(row.snapshot(null, null));
                    ClipboardContent cc = new ClipboardContent();
                    cc.put(SERIALIZED_MIME_TYPE, index);
                    db.setContent(cc);
                    event.consume();
                }
            });

            row.setOnDragOver(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                    if (row.getIndex() != ((Integer)db.getContent(SERIALIZED_MIME_TYPE)).intValue()) {
                        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                        event.consume();
                    }
                }
            });

            row.setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                    int draggedIndex = (Integer) db.getContent(SERIALIZED_MIME_TYPE);
                    Purchase draggedPurchase = (Purchase)tablePurchaseView.getItems().remove(draggedIndex);

                    int dropIndex ;

                    if (row.isEmpty()) {
                        dropIndex = tablePurchaseView.getItems().size() ;
                    } else {
                        dropIndex = row.getIndex();
                    }

                    tablePurchaseView.getItems().add(dropIndex, draggedPurchase);

                    event.setDropCompleted(true);
                    tablePurchaseView.getSelectionModel().select(dropIndex);
                    event.consume();
                }
            });
            return row ;
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
        columnPriceAll.setCellValueFactory(new PropertyValueFactory<Purchase, Double>("productPriceAll"));
        sumTable();
    }
}
