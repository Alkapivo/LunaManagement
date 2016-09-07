import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class OpenDialog {
    public static File openFile(String title, File directory) {
        FileChooser chooser = new FileChooser();
        chooser.setInitialDirectory(directory);
        chooser.setTitle(title);
        File fileToOpen = chooser.showOpenDialog(new Stage());
        return fileToOpen;
    }
    public static File openDirectory(String title, File directory) {
        DirectoryChooser chooser = new DirectoryChooser();
        File directoryToOpen = chooser.showDialog(new Stage());
        chooser.setInitialDirectory(directory);
        chooser.setTitle(title);
        return directoryToOpen;
    }
    public static File saveFile(String title, File directory) {
        FileChooser chooser = new FileChooser();
        chooser.setInitialDirectory(directory);
        chooser.setTitle(title);
        File fileToOpen = chooser.showSaveDialog(new Stage());
        return fileToOpen;
    }
}
