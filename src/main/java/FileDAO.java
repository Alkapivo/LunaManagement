import java.io.*;


public class FileDAO {
    public static void saveToFile(Bill bill, File fileToSave) {
        try {
            ObjectOutputStream save = new ObjectOutputStream(new FileOutputStream(fileToSave));
            save.writeObject(bill);
            save.flush();
            save.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static Bill loadFromFile(File fileToOpen) {
        try {
            ObjectInputStream load = new ObjectInputStream(new FileInputStream(fileToOpen));
            Bill bill = (Bill) load.readObject();
            load.close();
            return bill;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
