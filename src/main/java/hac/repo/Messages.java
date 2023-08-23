package hac.repo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * Messages bean, has id, author, message, receiver members.
 */
@Entity
public class Messages implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    private User author;

    @NotEmpty(message = "message is mandatory")
    private String message;

    @ManyToOne
    private User receiver;

    public Messages() {}

    public Messages(User author, String message, User receiver) {
        this.author = author;
        this.message = message;
        this.receiver = receiver;
    }

    public void setId(long id) {
        this.id=id;
    }

    public long getId() {return id;}

    public void setAuthor(User author) {
        this.author = author;
    }

    public User getAuthor() {
        return author;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public User getReceiver() {
        return this.receiver;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", Author=" + author + ", message=" + message + '}';
    }
}
