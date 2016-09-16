import org.junit.After;
import static org.junit.Assert.*;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FileDAOTest {
    @org.junit.Test
    public void fileDAOBillIsSaved() throws Exception{
        String testName = "Name";
        LocalDate testDate = LocalDate.of(2016,9,1);
        Purchase testPurchase = new Purchase("Test",1,10,10,100,125);
        List<Purchase> testList = new ArrayList<>();
        testList.add(testPurchase);
        Bill testBillSave = new Bill(testName,testDate,testList);
        File testFile = new File("test.lmb");
        FileDAO.saveToFile(testBillSave, testFile);
        assertEquals(testFile.exists(), true);
    }

    @org.junit.Test
    public void fileDAOBillIsLoaded()  {
        String testName = "Name";
        LocalDate testDate = LocalDate.of(2016,9,1);
        Purchase testPurchase = new Purchase("Test",1,10,10,100,125);
        List<Purchase> testList = new ArrayList<>();
        testList.add(testPurchase);
        Bill testBillSave = new Bill(testName,testDate,testList);
        File testFile = new File("test.lmb");
        FileDAO.saveToFile(testBillSave, testFile);

        Bill testLoad = FileDAO.loadFromFile(testFile);
        String result = testLoad.toString();
        String expected = testName+" "+testDate+" "+testList;
        assertEquals(expected,result);
    }

    @After
    public void fileDAOClear() throws Exception {
        File testFile = new File("test.lmb");
        testFile.delete();
    }
}
