package Model;

import java.util.LinkedList;
import java.util.Stack;

public class UserStore {
private java.util.Queue<UserModel> recentFoods = new java.util.LinkedList<>();

    // MENU (LinkedList)
    private LinkedList<UserModel> menuList = new LinkedList<>();

    // Deleted MENU items (Stack) - optional
    private Stack<UserModel> deletedMenuStack = new Stack<>();

    // ORDERS (Queue using LinkedList)
    private LinkedList<OrderModel> orderList = new LinkedList<>();

    // Completed ORDERS history (Stack)
    private Stack<OrderModel> orderHistory = new Stack<>();

    public void seedIfEmpty() {
        if (!menuList.isEmpty()) return;

        menuList.add(new UserModel("F001", "Momo", "Main Course", 140));
        menuList.add(new UserModel("F002", "Chowmein", "Main Course", 120));
        menuList.add(new UserModel("F003", "Fried Rice", "Main Course", 150));
        menuList.add(new UserModel("F004", "Tea", "Beverage", 30));
        menuList.add(new UserModel("F005", "Coffee", "Beverage", 80));
    }

    // ============== MENU GETTERS ==============

    public LinkedList<UserModel> getUsers() {
        return new LinkedList<>(menuList);
    }

    public UserModel findById(String id) {
        for (UserModel u : menuList) {
            if (u.getId().equalsIgnoreCase(id)) return u;
        }
        return null;
    }

    public boolean existsId(String id) {
        return findById(id) != null;
    }

    // ============== MENU CRUD (only if you are calling them) ==============

    public String addUser(UserModel item) {
        if (item == null) return "ERROR: Item is null.";

        String id = (item.getId() == null) ? "" : item.getId().trim();
        if (id.isEmpty()) return "ERROR: Item ID is required.";

        if (existsId(id)) return "ERROR: Item ID already exists.";

        menuList.add(item);
        return "OK";
    }

    public String updateUser(String id, String newName, String newCategory, double newPrice) {
        if (id == null || id.trim().isEmpty()) return "ERROR: Item ID is required.";

        UserModel found = findById(id.trim());
        if (found == null) return "ERROR: Item not found.";

        if (newName != null && !newName.trim().isEmpty()) found.setName(newName.trim());
        if (newCategory != null && !newCategory.trim().isEmpty()) found.setCategory(newCategory.trim());
        if (newPrice > 0) found.setPrice(newPrice);

        return "OK";
    }

    public String deleteUser(String id) {
        if (id == null || id.trim().isEmpty()) return "ERROR: Item ID is required.";

        UserModel found = findById(id.trim());
        if (found == null) return "ERROR: Item not found.";

        menuList.remove(found);
        deletedMenuStack.push(found); // ✅ correct: this stack stores UserModel
        return "OK";
    }

    // ============== ORDERS ==============

    public void addOrder(OrderModel order) {
        orderList.add(order);
    }

    public LinkedList<OrderModel> getAllOrders() {
        return new LinkedList<>(orderList);
    }

    // Needed because controller rebuilds the queue after removing one
    public void clearOrders() {
        orderList.clear();
    }

    // Push completed order to history stack
    public void pushToOrderHistory(OrderModel order) {
        orderHistory.push(order);
    }

    public Stack<OrderModel> getOrderHistory() {
        return orderHistory;
    }

    // ============== USER ORDER COUNT ==============

    public int getTotalOrdersForUser(String username) {
        int totalQty = 0;
        for (OrderModel o : orderList) {
            if (o.getUsername().equalsIgnoreCase(username)) {
                totalQty += o.getQuantity();
            }
        }
        return totalQty;
    }

    public LinkedList<String> getAllUsernamesWhoOrdered() {
        LinkedList<String> names = new LinkedList<>();
        for (OrderModel o : orderList) {
            boolean exists = false;
            for (String n : names) {
                if (n.equalsIgnoreCase(o.getUsername())) {
                    exists = true;
                    break;
                }
            }
            if (!exists) names.add(o.getUsername());
        }
        return names;
    }
    public void pushRecentFood(UserModel food) {
    if (food == null) return;

    recentFoods.add(food);

    // keep only last 5
    while (recentFoods.size() > 5) {
        recentFoods.poll(); // removes oldest
    }
}

public java.util.List<UserModel> getRecentFoods() {
    // return as list so view can loop easily
    return new java.util.ArrayList<>(recentFoods);
}

}
