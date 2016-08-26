import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Krolis on 2016-08-26.
 */
public class Main extends Application{

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("home_layout.fxml"));
        Scene scene = new Scene(root, 960, 540);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Luna Manager");
        primaryStage.show();
    }
}
