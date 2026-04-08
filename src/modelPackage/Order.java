package modelPackage;

import java.time.LocalDateTime;

public class Order {

    private int id;
    private LocalDateTime dateOrdered;
    private LocalDateTime dateCompleted;
    private LocalDateTime dateDelivered;
    private String status;
    private int tableId;

    public Order() {}

    public Order(int id, LocalDateTime dateOrdered, LocalDateTime dateCompleted,
                 LocalDateTime dateDelivered, String status, int tableId) {
        this.id = id;
        this.dateOrdered = dateOrdered;
        this.dateCompleted = dateCompleted;
        this.dateDelivered = dateDelivered;
        this.status = status;
        this.tableId = tableId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalDateTime getDateOrdered() { return dateOrdered; }
    public void setDateOrdered(LocalDateTime dateOrdered) { this.dateOrdered = dateOrdered; }

    public LocalDateTime getDateCompleted() { return dateCompleted; }
    public void setDateCompleted(LocalDateTime dateCompleted) { this.dateCompleted = dateCompleted; }

    public LocalDateTime getDateDelivered() { return dateDelivered; }
    public void setDateDelivered(LocalDateTime dateDelivered) { this.dateDelivered = dateDelivered; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getTableId() { return tableId; }
    public void setTableId(int tableId) { this.tableId = tableId; }
}