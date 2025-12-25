package Model;

import java.util.LinkedList;
import java.util.Stack;

public class UserStore {

    private LinkedList<UserModel> menuList = new LinkedList<>();
    private Stack<UserModel> deletedStack = new Stack<>();
    private LinkedList<OrderModel> orderList = new LinkedList<>();

    public void seedIfEmpty() {
        if (!menuList.isEmpty()) return;

        menuList.add(new UserModel("F001", "Momo", "Main Course", 140));
        menuList.add(new UserModel("F002", "Chowmein", "Main Course", 120));
        menuList.add(new UserModel("F003", "Fried Rice", "Main Course", 150));
        menuList.add(new UserModel("F004", "Tea", "Beverage", 30));
        menuList.add(new UserModel("F005", "Coffee", "Beverage", 80));
    }

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

    public void addUser(UserModel user) {
        menuList.add(user);
    }

    public void updateUser(String id, String name, String category) {
        updateUser(id, name, category, null);
    }

    public void updateUser(String id, String name, String category, Double price) {
        UserModel u = findById(id);
        if (u == null) return;

        if (name != null && !name.trim().isEmpty()) u.setName(name.trim());
        if (category != null && !category.trim().isEmpty()) u.setCategory(category.trim());
        if (price != null) u.setPrice(price);
    }

    public void deleteUser(String id) {
        UserModel u = findById(id);
        if (u == null) return;

        menuList.remove(u);
        deletedStack.push(u);
    }

    public boolean undoDelete() {
        if (deletedStack.isEmpty()) return false;
        menuList.add(deletedStack.pop());
        return true;
    }

    public void addOrder(OrderModel order) {
        orderList.add(order);
    }

    public LinkedList<OrderModel> getAllOrders() {
        return new LinkedList<>(orderList);
    }

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
}
