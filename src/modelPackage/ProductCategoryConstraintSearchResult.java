package modelPackage;

public class ProductCategoryConstraintSearchResult {

    private int productId;
    private String productName;
    private double productPrice;
    private String categoryName;
    private String constraintName;

    public ProductCategoryConstraintSearchResult(int productId, String productName, double productPrice,
                                                 String categoryName, String constraintName) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.categoryName = categoryName;
        this.constraintName = constraintName;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getConstraintName() {
        return constraintName;
    }
}