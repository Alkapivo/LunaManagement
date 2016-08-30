import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Krolis on 2016-08-26.
 */
public class HomeController implements Initializable{

    @FXML
    Button button_new_bill;

    @FXML
    Button button_toys_db;

    @FXML
    Button button_bills_db;

    public void initialize(URL location, ResourceBundle resources) {
        button_new_bill.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                // Z dokumentacji wyczytałem, że getWindow() zwraca Window natomiast
                // Window extends Stage więc możemy rzutować tak o:
                Stage stage = (Stage) button_new_bill.getScene().getWindow();
                //mamy stagea na którym pracujemy więc teraz ustawiamy mu nową scene
                // (najpierw ją tworzymy, podobnie jak w Main tylko dajemy inny fxml(no inny 'ekran')
                //blok try catch musimy dodać poniewać metoda FXMLLoader.load(..) wyrzuca wyjątek który musimy jakoś przechwycić
                try{
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("newBill_layout.fxml"));
                    loader.setController(new BillController());
                    Parent root = loader.load();
                    Scene scene = new Scene(root, 960, 540);
                    scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
                    stage.setScene(scene);
                }catch (IOException e){
                    System.out.println("HEJ! BŁĄD W CHUJ, CHYBA NIE ZANALAZŁO PLIKU FXML Z LAYOUTEM DO OTWARCIA " +
                            "ALBO NIE MASZ DO NIEGO DOSTĘPU " +
                            "ALBO COS INNEGO SIE ZJEBAŁO XDD");
                    e.printStackTrace();
                }

            }
        });

        button_toys_db.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                // TODO: 2016-08-26 go to toys database
            }
        });

        /**
         * Dokładnie to samo co dwa powyższe przypadki, ustawiamy eventhandlera do przechwytywania eventów na przycisku -
         * czyli np zwyczajnych kliknięć. Tutaj robimy to samo za pomocą lambdy <3
         */
        button_bills_db.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser chooser = new FileChooser();
                chooser.setTitle("Open File");
                File fileToOpen = chooser.showOpenDialog(new Stage());

                try{
                    Bill bill = FileDAO.loadFromFile(fileToOpen);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("newBill_layout.fxml"));
                    loader.setController(new BillController(bill));
                    Parent root = loader.load();
                    Scene scene = new Scene(root, 960, 540);
                    scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
                    Stage stage = (Stage) button_bills_db.getScene().getWindow();
                    stage.setScene(scene);
                }catch (IOException e){
                    e.printStackTrace();
                }

            }
        });
    }
}
