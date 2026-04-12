package modelPackage.entity;

import modelPackage.enums.OrderStatus;

import java.time.LocalDateTime;

public class Order {

    private int id;
    private LocalDateTime dateOrdered;
    private LocalDateTime dateCompleted;
    private LocalDateTime dateDelivered;
    private OrderStatus status;
    private boolean paid;
    private int tableId;

    public Order() {
    }

    public Order(int id, LocalDateTime dateOrdered, LocalDateTime dateCompleted,
                 LocalDateTime dateDelivered, OrderStatus status, boolean paid, int tableId) {
        setId(id);
        setDateOrdered(dateOrdered);
        setDateCompleted(dateCompleted);
        setDateDelivered(dateDelivered);
        setStatus(status);
        setPaid(paid);
        setTableId(tableId);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("Order id cannot be negative.");
        }
        this.id = id;
    }

    public LocalDateTime getDateOrdered() {
        return dateOrdered;
    }

    public void setDateOrdered(LocalDateTime dateOrdered) {
        if (dateOrdered == null) {
            throw new IllegalArgumentException("Date ordered is required.");
        }
        this.dateOrdered = dateOrdered;
    }

    public LocalDateTime getDateCompleted() {
        return dateCompleted;
    }

    public void setDateCompleted(LocalDateTime dateCompleted) {
        this.dateCompleted = dateCompleted;
    }

    public LocalDateTime getDateDelivered() {
        return dateDelivered;
    }

    public void setDateDelivered(LocalDateTime dateDelivered) {
        this.dateDelivered = dateDelivered;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status is required.");
        }
        this.status = status;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        if (tableId <= 0) {
            throw new IllegalArgumentException("Table id must be positive.");
        }
        this.tableId = tableId;
    }
}