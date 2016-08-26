import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

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
                    Parent root = FXMLLoader.load(getClass().getResource("newBill_layout.fxml"));
                    Scene scene = new Scene(root, 960, 540);
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
        button_bills_db.setOnAction(item->{
            System.out.println("lamda");
            // TODO: 2016-08-26 go to bills database
        });
    }
}
