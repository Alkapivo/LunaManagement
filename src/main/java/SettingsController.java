import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;


public class SettingsController implements Initializable {

    @FXML TextField tvLogin;
    @FXML PasswordField tvPassword;
    @FXML TextField tvFTP;
    @FXML TextField tvPort;
    @FXML Hyperlink hyperlinkRestore;
    @FXML Button buttonAccept;


    public void initialize(URL location, ResourceBundle resources) {
        tvLogin.setText(Settings.getLoginFTP());
        tvPassword.setText(Settings.getPasswordFTP());
        tvFTP.setText(Settings.getAddressFTP());
        tvPort.setText(Integer.toString(Settings.getPortFTP()));

        buttonAccept.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String login = tvLogin.getText().trim();
                String password = tvPassword.getText().trim();
                String address = tvFTP.getText().trim();
                int port;
                try {port = Integer.parseInt(tvPort.getText().trim());} catch (NumberFormatException e) {port = 21;}
                Settings.setFTPSettings(login,password,address,port);
                Settings.saveSettings();
            }
        });

        hyperlinkRestore.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Settings.restoreFTPSettings();
            }
        });

    }
}
