import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Aleksander on 15.09.2016.
 */
public class ValidatePurchaseTest {
    //NAME
    @org.junit.Test
    public void nameIsEmpty() throws Exception {
        boolean result = ValidatePurchase.validateName("");
        assertEquals(result,false);
    }
    @org.junit.Test
    public void nameIsNotEmpty() throws Exception {
        boolean result = ValidatePurchase.validateName("Test");
        assertEquals(result,true);
    }

    //COUNT
    @org.junit.Test
    public void countIsEmpty() throws Exception {
        boolean result = ValidatePurchase.validatePercent("");
        assertEquals(result,false);
    }
    @org.junit.Test
    public void countIsNotEmpty() throws Exception {
        boolean result = ValidatePurchase.validatePercent("1");
        assertEquals(result,true);
    }
    @org.junit.Test
    public void countRangeBelow() throws Exception {
        boolean result = ValidatePurchase.validatePercent("-10");
        assertEquals(result,true);
    }
    @org.junit.Test
    public void countRangeAbove() throws Exception {
        boolean result = ValidatePurchase.validatePercent("110");
        assertEquals(result,true);
    }
    @org.junit.Test
    public void countNumberFormat() throws Exception {
        boolean result = ValidatePurchase.validatePercent("A");
        assertEquals(result,false);
    }

    //PERCENT
    @org.junit.Test
    public void percentIsEmpty() throws Exception {
        boolean result = ValidatePurchase.validatePercent("");
        assertEquals(result,false);
    }
    @org.junit.Test
    public void percentIsNotEmpty() throws Exception {
        boolean result = ValidatePurchase.validatePercent("1");
        assertEquals(result,true);
    }
    @org.junit.Test
    public void percentRangeBelow() throws Exception {
        boolean result = ValidatePurchase.validatePercent("-10");
        assertEquals(result,true);
    }
    @org.junit.Test
    public void percentRangeAbove() throws Exception {
        boolean result = ValidatePurchase.validatePercent("110");
        assertEquals(result,true);
    }
    @org.junit.Test
    public void percentNumberFormat() throws Exception {
        boolean result = ValidatePurchase.validatePercent("A");
        assertEquals(result,false);
    }

    //PRICE
    @org.junit.Test
    public void priceIsEmpty() throws Exception {
        boolean result = ValidatePurchase.validatePrice("");
        assertEquals(result,false);
    }
    @org.junit.Test
    public void priceIsNotEmpty() throws Exception {
        boolean result = ValidatePurchase.validatePrice("1");
        assertEquals(result,true);
    }
    @org.junit.Test
    public void priceRangeBelow() throws Exception {
        boolean result = ValidatePurchase.validatePrice("-10");
        assertEquals(result,true);
    }
    @org.junit.Test
    public void priceNumberFormat() throws Exception {
        boolean result = ValidatePurchase.validatePrice("A");
        assertEquals(result,false);
    }

}