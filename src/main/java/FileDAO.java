import java.io.*;

/**
 * Created by Lenovo on 30.08.2016.
 */
public class FileDAO {
    public static void saveToFile(Bill bill) {
        try {
            ObjectOutputStream save = new ObjectOutputStream(new FileOutputStream("chuj.txt"));
            save.writeObject(bill);
            save.flush();
            save.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            //TODO
        }
    }
    public static Bill loadFromFile(File fileToOpen) {
        try {
            ObjectInputStream load = new ObjectInputStream(new FileInputStream(fileToOpen));
            Bill bill = (Bill)load.readObject();
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
