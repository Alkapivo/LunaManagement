import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Lenovo on 04.09.2016.
 */
public class HomeDirectory {
    private File mainDirectory = new File(".");
    private File recentDirectory = new File(".");

    public File getMainDirectory() {
        return mainDirectory;
    }

    public void setMainDirectory(File mainDirectory) {
        this.mainDirectory = mainDirectory;
    }
}
