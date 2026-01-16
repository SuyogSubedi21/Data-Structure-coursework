package Controller;

import Model.UserStore;
import Model.OrderModel;
import Model.UserModel;

import java.util.LinkedList;
import java.util.Stack;

public class AdminController {

    private final UserStore store;

    public AdminController(UserStore store) {
        this.store = store;
    }

    // ================= MENU (CRUD) =================

    // Return all menu items (copy)
    public LinkedList<UserModel> getAllMenuItems() {
        return store.getUsers();
    }

    // Add menu item
    public String addMenuItem(String id, String name, String category, double price) {
        UserModel item = new UserModel(id, name, category, price);
        return store.addUser(item);
        
    }

    // Update menu item
    public String updateMenuItem(String id, String name, String category, double price) {
        return store.updateUser(id, name, category, price);
    }

    // Delete menu item
    public String deleteMenuItem(String id) {
        return store.deleteUser(id);
    }

    // ================= ORDERS (Queue) =================

    // Add order (called from user side)
    public void addOrder(OrderModel order) {
        store.addOrder(order);
    }

    // Get all pending orders (copy)
    public LinkedList<OrderModel> getAllOrders() {
        return store.getAllOrders();
    }

    // ================= COMPLETE ORDER (Queue -> Stack) =================
    // index = selected row index in Orders table
    public OrderModel completeOrderAt(int index) {

        LinkedList<OrderModel> ordersCopy = store.getAllOrders();

        if (index < 0 || index >= ordersCopy.size()) {
            return null;
        }

        // Take the completed order from the copy
        OrderModel completed = ordersCopy.remove(index);

        // Now update the real store orderList by rebuilding it
        store.clearOrders();
        for (OrderModel o : ordersCopy) {
            store.addOrder(o);
        }

        // Push to order history stack
        store.pushToOrderHistory(completed);

        return completed;
    }

    // ================= ORDER HISTORY (Stack) =================

    public Stack<OrderModel> getOrderHistory() {
        return store.getOrderHistory();
    }

    // ================= USER ORDER COUNT =================

    public int getTotalOrdersForUser(String username) {
        return store.getTotalOrdersForUser(username);
    }
}
