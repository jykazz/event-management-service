package ru.rsreu.lutikov.sber.domain;

import javax.persistence.*;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @OneToMany(mappedBy = "id")
    private Set<Ticket> ticket;

    @OneToMany(mappedBy = "id")
    private Set<RefreshToken> refreshToken;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    @Column(unique = true)
    private String email;

    private boolean active;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set <Role> roles;

    public User() {
    }

    public User (String username, String password, String email, boolean active, Set<Role> role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.active = active;
        this.roles = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Role> getRole() {
        return roles;
    }

    public void setRole(Set<Role> role) {
        this.roles = role;
    }

    // Методы equals и hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String[] getRoles() {
        Iterator<Role> iterator = roles.iterator();
        String[] roleArray = new String[roles.size()];
        int i = 0;
        while(iterator.hasNext()) {
            roleArray[i] = iterator.next().name();
            i++;
        }
        return roleArray;
    }

    // Дополнительные методы
}
