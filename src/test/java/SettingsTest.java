import org.junit.After;
import java.io.File;
import static org.junit.Assert.*;

public class SettingsTest {
    @org.junit.Test
    public void settingsFileIsCreated() throws Exception {
        Settings.saveSettings();
        File test = new File("settings");
        assertEquals(test.exists(),true);
    }

    @org.junit.Test
    public void settingsHomeDirectoryExists() throws Exception {
        Settings.createSettings();
        assertEquals(Settings.getHomeDirectory().exists(),true);
    }

    @org.junit.Test
    public void settingsRecentDirectoryExists() throws Exception {
        Settings.createSettings();
        assertEquals(Settings.getRecentDirectory().exists(),true);
    }

    @After
    public void settingsClear() throws Exception {
        File test = new File("settings");
        test.delete();
    }

}