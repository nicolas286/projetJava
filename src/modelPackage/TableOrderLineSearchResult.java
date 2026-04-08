package modelPackage;

import java.time.LocalDateTime;

public class TableOrderLineSearchResult {

    private int tableId;
    private int positionX;
    private int positionY;
    private Integer floor;
    private int capacity;
    private boolean active;

    private int orderId;
    private LocalDateTime dateOrdered;
    private LocalDateTime dateCompleted;
    private LocalDateTime dateDelivered;
    private String status;

    private int lineNumber;
    private int productId;
    private String nameSnapshot;
    private double priceSnapshot;
    private int quantity;

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