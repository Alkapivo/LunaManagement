import java.io.Serializable;

/**
 * Created by Krolis on 2016-08-26.
 */
public class Purchase implements Serializable {
    private String productName;
    private int productCount;
    private int productTax;
    private double productNetto;
    private double productBrutto;


    public Purchase(String productName, int productCount, int productTax, double productNetto) {
        this.productName = productName;
        this.productCount = productCount;
        this.productTax = productTax;
        this.productNetto = productNetto;
        this.productBrutto = productNetto*(productTax/100.0)+productNetto;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }

    public int getProductTax() {
        return productTax;
    }

    public void setProductTax(int productTax) {
        this.productTax = productTax;
    }

    public double getProductNetto() {
        return productNetto;
    }

    public void setProductNetto(double productNetto) {
        this.productNetto = productNetto;
    }

    public double getProductBrutto() {
        return productBrutto;
    }

    public void setProductBrutto(double productBrutto) {
        this.productBrutto = productBrutto;
    }

    @Override
    public String toString() {
        String result = productName;
        return result;
    }
}
