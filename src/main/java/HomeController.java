import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.*;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Krolis on 2016-08-26.
 */
public class HomeController implements Initializable{

    @FXML
    Hyperlink hyperlinkNewBill;

    @FXML
    Hyperlink hyperlinkLoadBill;

    @FXML
    Canvas canvasLogo;

    @FXML
    Pane paneLogo;


    public void initialize(URL location, ResourceBundle resources) {

        //Loading settings

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
                    System.out.println("Exception homectrl");
                    e.printStackTrace();
                }
            }
        });

        hyperlinkLoadBill.setOnAction(new EventHandler<ActionEvent>() {
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
                    Scene scene = new Scene(root, 1114, 640);
                    scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
                    Stage stage = (Stage) hyperlinkNewBill.getScene().getWindow();
                    stage.setScene(scene);
                }catch (IOException e){
                    e.printStackTrace();
                }

            }
        });
        Image logoLuna = new Image(getClass().getResourceAsStream("logoLunaApp.png"));
        canvasLogo.getGraphicsContext2D().drawImage(logoLuna,0,0);
        paneLogo.setStyle("-fx-background-color: #34485b;");
    }
}
