import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class ChooseWindow {
    public static File openFile(String title, File directory) {
        FileChooser chooser = new FileChooser();
        chooser.setInitialDirectory(directory);
        chooser.setTitle(title);
        File fileToOpen = chooser.showOpenDialog(new Stage());
        return fileToOpen;
    }
}
