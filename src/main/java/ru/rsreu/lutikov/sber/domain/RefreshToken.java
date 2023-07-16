package ru.rsreu.lutikov.sber.domain;

import org.hibernate.annotations.SQLInsert;

import javax.persistence.*;

import javax.persistence.*;

@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String tokenValue;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public RefreshToken() {
    }

    public RefreshToken(String tokenValue, User user) {
        this.tokenValue = tokenValue;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
