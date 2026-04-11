package dataAccessPackage;

import java.util.Objects;

public class OrderLineId {

    private final int number;
    private final int orderId;

    public OrderLineId(int number, int orderId) {
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