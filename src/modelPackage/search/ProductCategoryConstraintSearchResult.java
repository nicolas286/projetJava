package modelPackage.search;

public class ProductCategoryConstraintSearchResult {

    private final int productId;
    private final String productName;
    private final double productPrice;
    private final String categoryName;
    private final String constraintName;

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