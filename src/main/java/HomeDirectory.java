import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Lenovo on 04.09.2016.
 */
public class HomeDirectory {
    private Path homeDirectory = Paths.get(".");


    public Path getHomeDirectory() {
        return homeDirectory;
    }

    public void setHomeDirectory(Path homeDirectory) {
        this.homeDirectory = homeDirectory;
    }

    public void createSettings() {
        File settingsFile = new File("settings.ini");
        if (settingsFile.exists() == true) {
            //Load from file

        }
        else {
            //Set default path
            this.homeDirectory = Paths.get(".");
            //Save to file

        }
    }

    public void loadSettings() {

    }
}
