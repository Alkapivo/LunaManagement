import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax.print.PrintService;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class HomeController implements Initializable{

    @FXML Hyperlink hyperlinkNewBill;
    @FXML Hyperlink hyperlinkLoadBill;
    @FXML Hyperlink hyperlinkPrintBill;
    @FXML Hyperlink hyperlinkChangeHomeDirectory;
    @FXML Canvas canvasLogo;
    @FXML Pane paneLogo;
    @FXML TableView tableRecentView;
    @FXML TableColumn columnName;
    @FXML Button buttonOpen;


    public void createBillWindow() {
        Stage stage = (Stage) hyperlinkNewBill.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("newBill_layout.fxml"));
            loader.setController(new BillController());
            Parent root = loader.load();
            Scene scene = new Scene(root, 1114, 640);
            stage.setScene(scene);
            stage.setResizable(false);
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
            stage.setResizable(false);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void initialize(URL location, ResourceBundle resources) {
        Settings.createSettings();
        Settings.loadSettings();
        RecentBillsList.createList();
        RecentBillsList.loadList();
        RecentBillsList.checkBillList();

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

        tableRecentView.getItems().addAll(RecentBillsList.getBillList());
        columnName.setCellValueFactory(new PropertyValueFactory<File, String>("name"));

        buttonOpen.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(tableRecentView.getSelectionModel().getSelectedItem() != null) {
                    int selectedRow = tableRecentView.getSelectionModel().getSelectedIndex();
                    File fileToOpen = (File)tableRecentView.getItems().get(selectedRow);
                    createLoadedBillWindow(fileToOpen);
                }
            }
        });

        Image logoLuna = new Image(getClass().getResourceAsStream("logoLunaApp.png"));
        canvasLogo.getGraphicsContext2D().drawImage(logoLuna,0,0);
        paneLogo.setStyle("-fx-background-color: #34485b;");
    }
}
