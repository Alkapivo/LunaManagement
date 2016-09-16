import org.junit.After;
import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

public class RecentBillsListTest {
    @org.junit.Test
    public void recentBillListFileIsCreated() throws Exception {
        RecentBillsList.saveList();
        File test = new File("recentFiles");
        assertEquals(test.exists(),true);
    }

    @org.junit.Test
    public void recentBillsListFileIsAdded() throws Exception {
        RecentBillsList.createList();
        File testFile = new File("test");
        RecentBillsList.addBill(testFile);
        List test = RecentBillsList.getBillList();
        String result = test.get(0).toString();
        String expected = "test";
        assertEquals(expected,result);
    }

    @org.junit.Test
    public void recentBillsListNoDuplicat() throws Exception {
        RecentBillsList.createList();
        File testFile = new File("test");
        for(int i=0; i<1; i++) {
            RecentBillsList.addBill(testFile);
        }
        int result = RecentBillsList.getBillList().size();
        int expected = 2;
        assertNotEquals(expected,result);
    }

    @org.junit.Test
    public void recentBillsListSizeIsOK() throws Exception {
        RecentBillsList.createList();
        for(int i=0; i<20; i++) {
            File testFile = new File("test"+i);
            RecentBillsList.addBill(testFile);
        }
        int result = RecentBillsList.getBillList().size();
        int expected = 10;
        assertEquals(expected,result);
    }

    @After
    public void recentFilesClear() throws Exception {
        File test = new File("recentFiles");
        test.delete();
    }
}