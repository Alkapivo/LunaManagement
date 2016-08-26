/**
 * Created by Krolis on 2016-08-26.
 */
public class Purchase {
    private String productName;
    private int productCount;


    public Purchase(String productName, int productCount) {
        this.productName = productName;
        this.productCount = productCount;
    }

    @Override
    public String toString() {
        return productName + " x " + productCount;
    }
}
