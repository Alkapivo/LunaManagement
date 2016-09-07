import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Settings {
    private static File homeDirectory;
    private static File recentDirectory;
    private static List settingsList;

    public static void createSettings() {
        homeDirectory = new File(".");
        recentDirectory = new File(".");
    }

    public static void loadSettings() {
        try {
            ObjectInputStream load = new ObjectInputStream(new FileInputStream("settings.txt"));
            settingsList = (ArrayList) load.readObject();
            load.close();
            System.out.println(settingsList.get(0).toString()+"\n"+settingsList.get(1).toString());
        }
        catch (IOException e) {
            saveSettings();
        }
        catch (ClassNotFoundException e) {
            saveSettings();
        }
        if (!homeDirectory.exists() || !recentDirectory.exists()) {
            createSettings();
            saveSettings();
        }
    }
    public static void saveSettings() {
        settingsList = new ArrayList();
        settingsList.add(0,homeDirectory);
        settingsList.add(1,recentDirectory);
        try {
            ObjectOutputStream save = new ObjectOutputStream(new FileOutputStream("settings.txt"));
            save.writeObject(settingsList);
            save.flush();
            save.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void setHomeDirectory(File homeDir) {
        homeDirectory = new File(homeDir.getParent());
        saveSettings();
    }
    public static void setRecentDirectory(File recentDir) {
        recentDirectory = new File(recentDir.getParent());
        saveSettings();
    }

    public static File getHomeDirectory() {
        return homeDirectory;
    }
    public static File getRecentDirectory() {
        return recentDirectory;
    }
}