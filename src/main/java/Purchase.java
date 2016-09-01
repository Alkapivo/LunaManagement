import java.io.Serializable;

/**
 * Created by Krolis on 2016-08-26.
 */
public class Purchase implements Serializable {
    private String productName;
    private int productCount;
    private int productTax;
    private int productMargin;
    private double productNetto;
    private double productNettoAll;
    private double productBrutto;
    private double productBruttoPrice;
    private double productPrice;
    private double productPriceAll;



    public Purchase(String productName, int productCount, int productTax, int productMargin, double productNetto, double productPrice) {
        this.productName = productName;
        this.productCount = productCount;
        this.productNetto = productNetto;
        this.productTax = productTax;
        this.productNettoAll = productNetto*productCount;
        this.productBrutto = productNetto*(productTax/100.0)+productNetto;
        this.productMargin = productMargin;
        this.productBruttoPrice = productNetto*(productTax/100.0)+productNetto*(productMargin/100.0)+productNetto;
        this.productPrice = productPrice;
        this.productPriceAll = productPrice*productCount;
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

    public int getProductMargin() {
        return productMargin;
    }

    public void setProductMargin(int productMargin) {
        this.productMargin = productMargin;
    }

    public double getProductNettoAll() {
        return productNettoAll;
    }

    public void setProductNettoAll(double productNettoAll) {
        this.productNettoAll = productNettoAll;
    }

    public double getProductBruttoPrice() {
        return productBruttoPrice;
    }

    public void setProductBruttoPrice(double productBruttoPrice) {
        this.productBruttoPrice = productBruttoPrice;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public double getProductPriceAll() {
        return productPriceAll;
    }

    public void setProductPriceAll(double productPriceAll) {
        this.productPriceAll = productPriceAll;
    }

    @Override
    public String toString() {
        String result = productName;
        return result;
    }
}
