package modelPackage.entity;

import java.util.Objects;

public class OrderLine {

    private int number;
    private int orderId;
    private String nameSnapshot;
    private double priceSnapshot;
    private int productId;
    private int quantity;

    public OrderLine() {
    }

    public OrderLine(int number, int orderId, String nameSnapshot, double priceSnapshot, int productId, int quantity) {
        setNumber(number);
        setOrderId(orderId);
        setNameSnapshot(nameSnapshot);
        setPriceSnapshot(priceSnapshot);
        setProductId(productId);
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

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        if (orderId <= 0) {
            throw new IllegalArgumentException("Order id must be positive.");
        }
        this.orderId = orderId;
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

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        if (productId <= 0) {
            throw new IllegalArgumentException("Product id must be positive.");
        }
        this.productId = productId;
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

    public static class OrderLineId {

        private final int number;
        private final int orderId;

        public OrderLineId(int number, int orderId) {
            if (number <= 0) {
                throw new IllegalArgumentException("Line number must be positive.");
            }
            if (orderId <= 0) {
                throw new IllegalArgumentException("Order id must be positive.");
            }

            this.number = number;
            this.orderId = orderId;
        }

        public int getNumber() {
            return number;
        }

        public int getOrderId() {
            return orderId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof OrderLineId that)) return false;
            return number == that.number && orderId == that.orderId;
        }

        @Override
        public int hashCode() {
            return Objects.hash(number, orderId);
        }
    }
}