package Controller;

public class Controller {

    // index 1..10 will be used for user1..user10
    private final int[] userOrderCount = new int[11];

    // ================= LOGIN =================
    public String authenticate(String username, String password) {

        if (username == null) username = "";
        if (password == null) password = "";

        username = username.trim();
        password = password.trim();

        if (username.isEmpty() && password.isEmpty()) {
            return "ERROR: Username and password are required.";
        }
        if (username.isEmpty()) {
            return "ERROR: Username is required.";
        }
        if (password.isEmpty()) {
            return "ERROR: Password is required.";
        }

        if (username.equals("admin1") && password.equals("admin123")) {
            return "ADMIN";
        }

        // users: user1..user10 with password user123
        if (password.equals("user123")) {
            int idx = getUserIndex(username);
            if (idx != -1) return "USER";
        }

        return "ERROR: Invalid username or password.";
    }

    // ================= ORDER COUNT TRACKING =================

    // Increase count by 1 order
    public void incrementOrderCount(String username) {
        int idx = getUserIndex(username);
        if (idx != -1) {
            userOrderCount[idx]++;
        }
    }

    // OPTIONAL: Increase by quantity (if you want "number of items ordered")
    public void addToOrderCount(String username, int qty) {
        int idx = getUserIndex(username);
        if (idx != -1) {
            if (qty < 1) qty = 1;
            userOrderCount[idx] += qty;
        }
    }

    // Get order count for a given user
    public int getOrderCount(String username) {
        int idx = getUserIndex(username);
        return (idx == -1) ? 0 : userOrderCount[idx];
    }

    // Convert "user7" -> 7
    private int getUserIndex(String username) {
        if (username == null) return -1;

        String u = username.trim().toLowerCase();
        if (!u.startsWith("user")) return -1;

        try {
            int num = Integer.parseInt(u.substring(4)); // after "user"
            if (num >= 1 && num <= 10) return num;
        } catch (NumberFormatException e) {
            return -1;
        }
        return -1;
    }
}
