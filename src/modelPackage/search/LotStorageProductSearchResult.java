package modelPackage.search;

public class LotStorageProductSearchResult {

    private int lotId;
    private int quantity;
    private double price;
    private int productId;
    private String productName;
    private int storageId;
    private boolean refrigerated;

    public LotStorageProductSearchResult(int lotId, int quantity, double price,
                                         int productId, String productName,
                                         int storageId, boolean refrigerated) {
        this.lotId = lotId;
        this.quantity = quantity;
        this.price = price;
        this.productId = productId;
        this.productName = productName;
        this.storageId = storageId;
        this.refrigerated = refrigerated;
    }

    public int getLotId() {
        return lotId;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getStorageId() {
        return storageId;
    }

    public boolean isRefrigerated() {
        return refrigerated;
    }
}