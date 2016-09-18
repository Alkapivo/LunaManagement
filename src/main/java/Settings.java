import java.io.*;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;


public class Settings {
    private static File homeDirectory;
    private static File recentDirectory;
    private static List settingsList;
    private static String loginFTP;
    private static String passwordFTP;
    private static String addressFTP;
    private static int portFTP;

    public static void createSettings() {
        homeDirectory = new File(".");
        recentDirectory = new File(".");
        settingsList = new ArrayList();
        restoreFTPSettings();
    }

    public static void loadSettings() {
        try {
            ObjectInputStream load = new ObjectInputStream(new FileInputStream("settings"));
            settingsList = (ArrayList) load.readObject();
            load.close();
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
        settingsList.add(2,loginFTP);
        settingsList.add(3,passwordFTP);
        settingsList.add(4,addressFTP);
        settingsList.add(5,portFTP);
        try {
            ObjectOutputStream save = new ObjectOutputStream(new FileOutputStream("settings"));
            save.writeObject(settingsList);
            save.flush();
            save.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setFTPSettings(String login, String password, String address, int port) {
        loginFTP = login;
        passwordFTP = password;
        addressFTP = address;
        portFTP = port;
    }

    public static void restoreFTPSettings() {
        loginFTP = "lunaClient";
        passwordFTP = "RP6-MY1-89L";
        addressFTP = "lunamanagement.asuscomm.com";
        portFTP = 21;
    }

    public static String getLoginFTP() {
        return loginFTP;
    }

    public static String getPasswordFTP() {
        return passwordFTP;
    }

    public static String getAddressFTP() {
        return addressFTP;
    }

    public static int getPortFTP() {
        return portFTP;
    }

    public static void setHomeDirectory(File homeDir) {
        homeDirectory = new File(homeDir.getParent());
        saveSettings();
    }
    public static void setRecentDirectory(File recentDir) {
        recentDirectory = new File(recentDir.getParent());
        saveSettings();
    }
    public static File getBillDirectory(File homeDir,int year, Month month) {
        String filepath = homeDir.toString()+"/Database/"+Integer.toString(year)+"/"+month.toString();
        File checkDatabase = new File(homeDir.toString()+"/Database");
        File checkDir = new File(homeDir.toString()+"/Database/"+Integer.toString(year));
        File checkMonth = new File(homeDir.toString()+"/Database/"+Integer.toString(year)+"/"+month.toString());

        if (!(checkDatabase.exists() || checkDatabase.isDirectory())) {checkDatabase.mkdir();}
        if (!(checkDir.exists() || checkDir.isDirectory())) {checkDir.mkdir();}
        if (!(checkMonth.exists() || checkMonth.isDirectory())) {checkMonth.mkdir();}

        System.out.println(filepath);
        File billDirectory = new File(filepath);
        return billDirectory;
    }

    public static File getHomeDirectory() {
        return homeDirectory;
    }
    public static File getRecentDirectory() {
        return recentDirectory;
    }
}