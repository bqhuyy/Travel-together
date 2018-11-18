package cs300.apcs04.traveltogether;

public class User {
    private String name;
    private String email;
    public User() {
        name = "";
        email = "";
    }
    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
