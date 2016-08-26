import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

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
                // TODO: 2016-08-26 todo go to newBill
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
