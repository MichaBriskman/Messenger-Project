package hac.repo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * User Bean, has id, userName, password and confirmPassword members.
 */
@Entity
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotEmpty(message = "userName is mandatory")
    private String userName;

    @NotEmpty(message = "password is mandatory")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).+$", message = "Password must contain at least one letter and one number")
    private String password;
    @NotEmpty(message = "Confirm password is mandatory")
    private String confirmPassword;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Messages> authoredMessages;

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Messages> receivedMessages;

    public List<Messages> getAuthoredMessages() {
        return authoredMessages;
    }

    public List<Messages> getReceivedMessages() {
        return receivedMessages;
    }
    public User() {}

    public User(String userName, String password, String confirmPassword) {
        this.userName = userName;
        this.password = password;
        this.confirmPassword= confirmPassword;
    }

    public void setId(long id) {
        this.id = id;
    }
    public long getId() {
        return id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUserName() {
        return userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() { return password; }
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword=confirmPassword;
    }
    public String getConfirmPassword() {
        return confirmPassword;
    }
    public boolean checkPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(password, this.password);

    }
    public void setBCrypt() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.password = passwordEncoder.encode(this.password);
    }
    public void setConfirm() {
        this.confirmPassword=this.password;
    }
    @Override
    public String toString() {
        return "User{" + "id=" + id + ", userName=" + userName + ", password=" + password + '}';
    }
}
