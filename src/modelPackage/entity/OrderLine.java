package modelPackage.entity;

public class OrderLine {

    private int number;
    private Product product;
    private String nameSnapshot;
    private double priceSnapshot;
    private int quantity;

    public OrderLine() {
    }

    public OrderLine(int number, Product product, String nameSnapshot, double priceSnapshot, int quantity) {
        setNumber(number);
        setProduct(product);
        setNameSnapshot(nameSnapshot);
        setPriceSnapshot(priceSnapshot);
        setQuantity(quantity);
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        if (number <= 0) {
            throw new IllegalArgumentException("Line number must be positive.");
        }
        this.number = number;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product is required.");
        }
        this.product = product;
    }

    public String getNameSnapshot() {
        return nameSnapshot;
    }

    public void setNameSnapshot(String nameSnapshot) {
        if (nameSnapshot == null || nameSnapshot.isBlank()) {
            throw new IllegalArgumentException("Line name snapshot is required.");
        }
        this.nameSnapshot = nameSnapshot;
    }

    public double getPriceSnapshot() {
        return priceSnapshot;
    }

    public void setPriceSnapshot(double priceSnapshot) {
        if (priceSnapshot < 0) {
            throw new IllegalArgumentException("Price snapshot must be non-negative.");
        }
        this.priceSnapshot = priceSnapshot;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive.");
        }
        this.quantity = quantity;
    }

    public double getLineTotal() {
        return priceSnapshot * quantity;
    }
}