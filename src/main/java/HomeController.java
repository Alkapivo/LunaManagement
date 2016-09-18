import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax.print.PrintService;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.time.Month;
import java.time.Year;
import java.util.*;


public class HomeController implements Initializable{

    @FXML Canvas canvasLogo;
    @FXML Pane paneLogo;

    @FXML Hyperlink hyperlinkNewBill;
    @FXML Hyperlink hyperlinkLoadBill;
    @FXML Hyperlink hyperlinkPrintBill;
    @FXML Hyperlink hyperlinkChangeHomeDirectory;
    @FXML Hyperlink hyperlinkSettings;

    @FXML TableView tableRecentView;
    @FXML TableColumn columnNameRecent;
    @FXML Button buttonOpenRecent;

    @FXML TableView tableDatabaseView;
    @FXML TableColumn columnNameDatabase;
    @FXML Button buttonOpenDatabase;
    @FXML Button buttonDeleteDatabase;
    @FXML ChoiceBox chYear;
    @FXML ChoiceBox chMonth;
    @FXML Hyperlink hyperlinkDownloadDatabase;


    public void createBillWindow() {
        Stage stage = (Stage) hyperlinkNewBill.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("newBill_layout.fxml"));
            loader.setController(new BillController());
            Parent root = loader.load();
            Scene scene = new Scene(root, 1114, 640);
            stage.setScene(scene);
            stage.setMinWidth(1114);
            stage.setMinHeight(664);
            stage.setResizable(true);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createLoadedBillWindow(File fileToOpen) {
        try{
            Bill bill = FileDAO.loadFromFile(fileToOpen);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("newBill_layout.fxml"));
            loader.setController(new BillController(bill));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1114, 640);
            Stage stage = (Stage) hyperlinkNewBill.getScene().getWindow();
            stage.setScene(scene);
            stage.setMinWidth(1114);
            stage.setMinHeight(664);;
            stage.setResizable(true);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void databaseExecute(int selectedYear, int selectedMonth) {
        String dirYear = String.valueOf(selectedYear);
        String dirMonth = Month.of(selectedMonth).toString();
        File searchDirectory = new File("./Database/"+dirYear+"/"+dirMonth);
        FileFilter filefilter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                String extension = pathname.getName();
                extension = extension.substring(extension.length()-4);
                return extension.equals(".lmb");
            }
        };
        tableDatabaseView.getItems().clear();
        if (searchDirectory.exists() && searchDirectory.isDirectory()) {
            File[] listFiles = searchDirectory.listFiles(filefilter);
            tableDatabaseView.getItems().addAll(listFiles);
            columnNameDatabase.setCellValueFactory(new PropertyValueFactory<File, String>("name"));
        }
    }

    public void initialize(URL location, ResourceBundle resources) {
        Settings.createSettings();
        Settings.loadSettings();
        RecentBillsList.createList();
        RecentBillsList.loadList();
        RecentBillsList.checkBillList();
        Image logoLuna = new Image(getClass().getResourceAsStream("logoLunaApp.png"));
        canvasLogo.getGraphicsContext2D().drawImage(logoLuna,0,0);
        paneLogo.setStyle("-fx-background-color: #34485b;");

        int actYear = Integer.parseInt(Year.now().toString());
        ObservableList<String> yearList = FXCollections.observableArrayList();
        for(int i=2016; i<=actYear; i++) {
            yearList.add(Integer.toString(i));
        }
        chYear.setItems(yearList);
        chMonth.setItems(FXCollections.observableArrayList("styczeń", "luty", "marzec", "kwiecień", "maj", "czerwiec", "lipiec", "sierpień", "wrzesień", "październik", "listopad", "grudzień"));
        chYear.getSelectionModel().selectFirst();
        chMonth.getSelectionModel().selectFirst();

        hyperlinkNewBill.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                createBillWindow();
            }
        });

        hyperlinkLoadBill.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File fileToOpen = OpenDialog.openFile("Otwórz fakturę", Settings.getRecentDirectory(),"*.lmb", "Luna Management Bill (*.lmb)");
                Settings.setRecentDirectory(fileToOpen);
                createLoadedBillWindow(fileToOpen);
            }
        });

        hyperlinkPrintBill.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File fileToOpen = OpenDialog.openFile("Drukuj fakturę PDF", Settings.getRecentDirectory(),"*.pdf", "PDF file (*.pdf)");
                Settings.setRecentDirectory(fileToOpen);
                PrintService service = PDFPrint.choosePrinter();
                PDFPrint.printPDF(fileToOpen, service);
            }
        });

        hyperlinkChangeHomeDirectory.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File homeDirectory = OpenDialog.openDirectory("Wybierz katalog domowy", Settings.getRecentDirectory());
                Settings.setRecentDirectory(homeDirectory);
                Settings.setHomeDirectory(homeDirectory);
            }
        });

        hyperlinkSettings.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("settings_layout.fxml"));
                    loader.setController(new SettingsController());
                    Parent root = loader.load();
                    Scene scene = new Scene(root, 320, 266);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.setTitle("Ustawienia FTP");
                    stage.setResizable(false);
                    stage.show();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        buttonOpenDatabase.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(tableDatabaseView.getSelectionModel().getSelectedItem() != null) {
                    int selectedRow = tableDatabaseView.getSelectionModel().getSelectedIndex();
                    File fileToOpen = (File)tableDatabaseView.getItems().get(selectedRow);
                    createLoadedBillWindow(fileToOpen);
                }
            }
        });

        hyperlinkDownloadDatabase.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File databasePath = new File(Settings.getHomeDirectory().toString()+"/Database/");
                if (!databasePath.exists() || !databasePath.isDirectory()) {

                }
            }
        });

        buttonDeleteDatabase.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Ostrzeżenie");
                alert.setHeaderText("Tej operacji nie da się cofnąć");
                alert.setContentText("Czy na pewno chcesz kontynuować?");

                ButtonType buttonTypeOne = new ButtonType("Tak");
                ButtonType buttonTypeTwo = new ButtonType("Nie");

                alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == buttonTypeOne) {
                    if(tableDatabaseView.getSelectionModel().getSelectedItem() != null) {
                        int selectedRow = tableDatabaseView.getSelectionModel().getSelectedIndex();
                        File fileToRemove = (File)tableDatabaseView.getItems().get(selectedRow);
                        fileToRemove.delete();
                        tableDatabaseView.getItems().remove(selectedRow);
                    }
                }
            }
        });

        buttonOpenRecent.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(tableRecentView.getSelectionModel().getSelectedItem() != null) {
                    int selectedRow = tableRecentView.getSelectionModel().getSelectedIndex();
                    File fileToOpen = (File)tableRecentView.getItems().get(selectedRow);
                    createLoadedBillWindow(fileToOpen);
                }
            }
        });

        chYear.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                int selectedYear = newValue.intValue();
                int selectedMonth = (int)chMonth.getValue()+1;
                databaseExecute(selectedYear,selectedMonth);
            }
        });

        chMonth.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                int selectedYear = Integer.parseInt(chYear.getValue().toString());
                int selectedMonth = newValue.intValue()+1;
                databaseExecute(selectedYear,selectedMonth);
            }
        });

        tableRecentView.getItems().addAll(RecentBillsList.getBillList());
        columnNameRecent.setCellValueFactory(new PropertyValueFactory<File, String>("name"));


    }
}
