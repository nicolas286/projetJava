package modelPackage.entity;

import modelPackage.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Order {

    private int id;
    private LocalDateTime dateOrdered;
    private LocalDateTime dateCompleted;
    private LocalDateTime dateDelivered;
    private OrderStatus status;
    private boolean paid;
    private RestaurantTable table;
    private final List<OrderLine> orderLines;

    public Order(int id, LocalDateTime dateOrdered, LocalDateTime dateCompleted,
                 LocalDateTime dateDelivered, OrderStatus status, boolean paid, RestaurantTable table) {
        setId(id);
        setDateOrdered(dateOrdered);
        setDateCompleted(dateCompleted);
        setDateDelivered(dateDelivered);
        setStatus(status);
        setPaid(paid);
        setTable(table);
        this.orderLines = new ArrayList<>();
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

    public RestaurantTable getTable() {
        return table;
    }

    public void setTable(RestaurantTable table) {
        if (table == null) {
            throw new IllegalArgumentException("Table is required.");
        }
        this.table = table;
    }

    public List<OrderLine> getOrderLines() {
        return Collections.unmodifiableList(orderLines);
    }

    public void addOrderLine(OrderLine orderLine) {
        if (orderLine == null) {
            throw new IllegalArgumentException("Order line cannot be null.");
        }
        orderLines.add(orderLine);
    }

    public void removeOrderLine(OrderLine orderLine) {
        if (orderLine == null) {
            throw new IllegalArgumentException("Order line cannot be null.");
        }
        orderLines.remove(orderLine);
    }
}