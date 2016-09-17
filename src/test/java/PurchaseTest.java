import static org.junit.Assert.*;


public class PurchaseTest {
    @org.junit.Test
    public void purchaseBruttoPriceIsOK() throws Exception {
        Purchase test = new Purchase("Name",1,10,10,100,125);
        double result = test.getProductBruttoPrice();
        double expected =  121.0;
        assertEquals(expected,result,0.0);
    }
}
