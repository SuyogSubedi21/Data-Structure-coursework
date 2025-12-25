package Model;

public class UserModel {

    private String id;
    private String name;
    private String group;

    // Represents one record shown in tables
    public UserModel(String id, String name, String group) {
        this.id = id;
        this.name = name;
        this.group = group;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getGroup() { return group; }

    public void setName(String name) { this.name = name; }
    public void setGroup(String group) { this.group = group; }
}
