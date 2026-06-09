import java.io.Serializable;
import java.time.LocalDateTime;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private UserIdentifier identifier;
    private String password;
    private LocalDateTime registrationDate;
    private LocalDateTime lastLoginDate;

    private transient boolean isLoggedIn;

    public User(UserIdentifier identifier, String password) {
        this.identifier = identifier;
        this.password = password;
        this.registrationDate = LocalDateTime.now();
        this.lastLoginDate = null;
        this.isLoggedIn = false;
    }

    public UserIdentifier getIdentifier() {
        return identifier;
    }

    public String getName() {
        return identifier.getName();
    }

    public int getId() {
        return identifier.getId();
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public void login() {
        this.isLoggedIn = true;
        this.lastLoginDate = LocalDateTime.now();
    }

    public void logout() {
        this.isLoggedIn = false;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public LocalDateTime getLastLoginDate() {
        return lastLoginDate;
    }

    public void resetLoginState() {
        this.isLoggedIn = false;
    }
}
