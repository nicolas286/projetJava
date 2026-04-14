package modelPackage.entity;

public class Product {

    private int id;
    private String name;
    private double price;
    private int lotId;

    public Product() {
    }

    public Product(int id, String name, double price, int lotId) {
        setId(id);
        setName(name);
        setPrice(price);
        setLotId(lotId);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Product id must be positive.");
        }
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Product name is required.");
        }
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Product price must be non-negative.");
        }
        this.price = price;
    }

    public int getLotId() {
        return lotId;
    }

    public void setLotId(int lotId) {
        if (lotId <= 0) {
            throw new IllegalArgumentException("Lot id must be positive.");
        }
        this.lotId = lotId;
    }

    @Override
    public String toString() {
        return name + " - " + price + " €";
    }
}