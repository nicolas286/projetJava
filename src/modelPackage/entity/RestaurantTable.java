package modelPackage.entity;

import java.util.Objects;

public class RestaurantTable {
    private int id;
    private int positionX;
    private int positionY;
    private int floor;
    private int capacity;
    private boolean active;

    public RestaurantTable() {
    }

    public RestaurantTable(int id, int positionX, int positionY, int floor, int capacity, boolean active) {
        setId(id);
        setPositionX(positionX);
        setPositionY(positionY);
        setFloor(floor);
        setCapacity(capacity);
        setActive(active);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Table id must be positive.");
        }
        this.id = id;
    }

    public void setPositionX(int positionX) {
        if (positionX < 0) {
            throw new IllegalArgumentException("Position X must be non-negative.");
        }
        this.positionX = positionX;
    }

    public void setPositionY(int positionY) {
        if (positionY < 0) {
            throw new IllegalArgumentException("Position Y must be non-negative.");
        }
        this.positionY = positionY;
    }

    public void setFloor(Integer floor) {
        if (floor != null && floor < 0) {
            throw new IllegalArgumentException("Floor must be non-negative.");
        }
        this.floor = floor;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive.");
        }
        this.capacity = capacity;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Table " + id + " (" + capacity + " seats)";
    }
}