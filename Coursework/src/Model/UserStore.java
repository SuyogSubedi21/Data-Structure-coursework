package Model;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class UserStore {

    private Queue<UserModel> userQueue = new LinkedList<>();
    private Stack<UserModel> deletedStack = new Stack<>();

    // Loads initial records for dashboard
    public void seedIfEmpty() {
        if (!userQueue.isEmpty()) return;

        userQueue.add(new UserModel("U001", "Ram", "A"));
        userQueue.add(new UserModel("U002", "Sita", "B"));
        userQueue.add(new UserModel("U003", "Hari", "A"));
        userQueue.add(new UserModel("U004", "Gita", "C"));
        userQueue.add(new UserModel("U005", "Nabin", "B"));
    }

    public Queue<UserModel> getUsers() {
        return new LinkedList<>(userQueue);
    }

    public boolean existsId(String id) {
        for (UserModel u : userQueue) {
            if (u.getId().equalsIgnoreCase(id)) return true;
        }
        return false;
    }

    public void addUser(UserModel user) {
        userQueue.add(user);
    }

    public void updateUser(String id, String name, String group) {
        Queue<UserModel> temp = new LinkedList<>();
        while (!userQueue.isEmpty()) {
            UserModel u = userQueue.poll();
            if (u.getId().equalsIgnoreCase(id)) {
                if (!name.isEmpty()) u.setName(name);
                if (!group.isEmpty()) u.setGroup(group);
            }
            temp.add(u);
        }
        userQueue = temp;
    }

    public void deleteUser(String id) {
        Queue<UserModel> temp = new LinkedList<>();
        while (!userQueue.isEmpty()) {
            UserModel u = userQueue.poll();
            if (u.getId().equalsIgnoreCase(id)) {
                deletedStack.push(u);
            } else {
                temp.add(u);
            }
        }
        userQueue = temp;
    }

    public boolean undoDelete() {
        if (deletedStack.isEmpty()) return false;
        userQueue.add(deletedStack.pop());
        return true;
    }
}
