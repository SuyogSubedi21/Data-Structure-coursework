package Controller;

import Model.UserModel;
import Model.UserStore;
import java.util.Queue;

public class AdminController {

    private UserStore store;

    public AdminController(UserStore store) {
        this.store = store;
    }

    public void initData() {
        store.seedIfEmpty();
    }

    public Queue<UserModel> getAllUsers() {
        return store.getUsers();
    }

    public String addUser(String id, String name, String group) {
        id = safe(id);
        name = safe(name);
        group = safe(group);

        if (id.isEmpty() || name.isEmpty() || group.isEmpty()) {
            return "ERROR: All fields are required.";
        }

        if (store.existsId(id)) return "ERROR: ID already exists.";

        store.addUser(new UserModel(id, name, group));
        return "OK";
    }

    public String updateUser(String id, String name, String group) {
        id = safe(id);
        if (id.isEmpty()) return "ERROR: ID required.";
        if (!store.existsId(id)) return "ERROR: ID not found.";

        store.updateUser(id, safe(name), safe(group));
        return "OK";
    }

    public String deleteUser(String id) {
        id = safe(id);
        if (id.isEmpty()) return "ERROR: ID required.";
        if (!store.existsId(id)) return "ERROR: ID not found.";

        store.deleteUser(id);
        return "OK";
    }

    public String undoDelete() {
        return store.undoDelete() ? "OK" : "ERROR: Nothing to undo.";
    }

    private String safe(String s) {
        return s == null ? "" : s.trim();
    }
}
