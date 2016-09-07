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


    public void initialize(URL location, ResourceBundle resources) {
        Settings.createSettings();
        Settings.loadSettings();
        RecentBillsList.createList();
        RecentBillsList.loadList();

        hyperlinkNewBill.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = (Stage) hyperlinkNewBill.getScene().getWindow();
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("newBill_layout.fxml"));
                    loader.setController(new BillController());
                    Parent root = loader.load();
                    Scene scene = new Scene(root, 1114, 640);
                    scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
                    stage.setScene(scene);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        hyperlinkLoadBill.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File fileToOpen = OpenDialog.openFile("Otwórz fakturę", Settings.getRecentDirectory());
                Settings.setRecentDirectory(fileToOpen);
                try{
                    Bill bill = FileDAO.loadFromFile(fileToOpen);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("newBill_layout.fxml"));
                    loader.setController(new BillController(bill));
                    Parent root = loader.load();
                    Scene scene = new Scene(root, 1114, 640);
                    scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
                    Stage stage = (Stage) hyperlinkNewBill.getScene().getWindow();
                    stage.setScene(scene);
                }
                catch (IOException e){
                    e.printStackTrace();
                }

            }
        });

        hyperlinkPrintBill.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File fileToOpen = OpenDialog.openFile("Drukuj fakturę PDF", Settings.getRecentDirectory());
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

        RecentBillsList.addBill(new File("test1"));
        RecentBillsList.addBill(new File("test2"));
        tableRecentView.getItems().addAll(RecentBillsList.getBillList());
        //TODO

        Image logoLuna = new Image(getClass().getResourceAsStream("logoLunaApp.png"));
        canvasLogo.getGraphicsContext2D().drawImage(logoLuna,0,0);
        paneLogo.setStyle("-fx-background-color: #34485b;");
    }
}
