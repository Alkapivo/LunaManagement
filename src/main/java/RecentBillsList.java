import java.io.*;
import java.util.ArrayList;
import java.util.List;


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
        if(!billList.contains(billFile)) {
            if(billList.size() > 9)
                billList.remove(billList.size() - 1);
            billList.add(0,billFile);
        }
    }

    public static List getBillList() {
        return billList;
    }

    public static void checkBillList() {
        for(int i=0; i<billList.size(); i++) {
            File tempFile = (File)billList.get(i);
            if (!tempFile.exists()) {
                billList.remove(i);
                i -= 1;
            }
        }
    }
}
