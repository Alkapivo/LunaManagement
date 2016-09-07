import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 04.09.2016.
 */
public class RecentBillsList {
    private static List billList;

    public static void createList() {
        billList = new ArrayList();
    }

    public static void loadList() {
        try {
            ObjectInputStream load = new ObjectInputStream(new FileInputStream("recentFiles"));
            billList = (ArrayList) load.readObject();
            load.close();
        }
        catch (IOException e) {
            saveList();
        }
        catch (ClassNotFoundException e) {
            saveList();
        }
    }
    public static void saveList() {
        try {
            ObjectOutputStream save = new ObjectOutputStream(new FileOutputStream("recentFiles"));
            save.writeObject(billList);
            save.flush();
            save.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void addBill(File billFile) {
        billList.add(billFile);
    }

    public static List getBillList() {
        return billList;
    }
}
