package library.radiomickyandonie.model;

/**
 * Created by lobo on 03-26-18.
 */

public class User {
    private String displayName;
    private String email;
    private boolean online;

    public User(String displayName, String email, boolean online) {
        this.displayName = displayName;
        this.email = email;
        this.online = online;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }

    public boolean isOnline() {
        return online;
    }
}
