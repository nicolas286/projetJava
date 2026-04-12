package modelPackage.search;

import java.time.LocalDateTime;

public class TableOrderLineSearchResult {

    private final int tableId;
    private final int positionX;
    private final int positionY;
    private final Integer floor;
    private final int capacity;
    private final boolean active;

    private final int orderId;
    private final LocalDateTime dateOrdered;
    private final LocalDateTime dateCompleted;
    private final LocalDateTime dateDelivered;
    private final String status;

    private final int lineNumber;
    private final int productId;
    private final String nameSnapshot;
    private final double priceSnapshot;
    private final int quantity;

    public TableOrderLineSearchResult(int tableId, int positionX, int positionY, Integer floor, int capacity, boolean active,
                                      int orderId, LocalDateTime dateOrdered, LocalDateTime dateCompleted,
                                      LocalDateTime dateDelivered, String status,
                                      int lineNumber, int productId, String nameSnapshot, double priceSnapshot, int quantity) {
        this.tableId = tableId;
        this.positionX = positionX;
        this.positionY = positionY;
        this.floor = floor;
        this.capacity = capacity;
        this.active = active;
        this.orderId = orderId;
        this.dateOrdered = dateOrdered;
        this.dateCompleted = dateCompleted;
        this.dateDelivered = dateDelivered;
        this.status = status;
        this.lineNumber = lineNumber;
        this.productId = productId;
        this.nameSnapshot = nameSnapshot;
        this.priceSnapshot = priceSnapshot;
        this.quantity = quantity;
    }

    public int getTableId() { return tableId; }
    public int getPositionX() { return positionX; }
    public int getPositionY() { return positionY; }
    public Integer getFloor() { return floor; }
    public int getCapacity() { return capacity; }
    public boolean isActive() { return active; }
    public int getOrderId() { return orderId; }
    public LocalDateTime getDateOrdered() { return dateOrdered; }
    public LocalDateTime getDateCompleted() { return dateCompleted; }
    public LocalDateTime getDateDelivered() { return dateDelivered; }
    public String getStatus() { return status; }
    public int getLineNumber() { return lineNumber; }
    public int getProductId() { return productId; }
    public String getNameSnapshot() { return nameSnapshot; }
    public double getPriceSnapshot() { return priceSnapshot; }
    public int getQuantity() { return quantity; }
}