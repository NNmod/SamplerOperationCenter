package entity;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "operators")
public class Operator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String login;

    private String password;

    private LocalDateTime createdAt;

    public Operator() {}

    public Operator(String login, String password) {
        this.login = login;
        this.password = password;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getLogin() { return login; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}