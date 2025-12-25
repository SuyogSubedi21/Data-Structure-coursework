package Model;

public class OrderModel {
    private String username;
    private String itemId;
    private String itemName;
    private int quantity;
    private double price;

    public OrderModel(String username, String itemId, String itemName, int quantity, double price) {
        this.username = username;
        this.itemId = itemId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.price = price;
    }

    public String getUsername() { return username; }
    public String getItemId() { return itemId; }
    public String getItemName() { return itemName; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }

    public double getTotal() { return price * quantity; }
}
