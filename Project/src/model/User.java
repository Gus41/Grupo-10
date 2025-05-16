package model;

import java.util.Objects;

public class User {
    private String username;
    private String password; 
    private String contact;

    public User(String username, String password, String contact) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty.");
        }
        
        this.username = username;
        this.password = password; 
        this.contact = contact;
    }

    public User() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty.");
        }
        this.username = username;
    }

    public String getPassword() {
             return password;
    }

    public void setPassword(String password) {
        
        this.password = password;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @Override
    public String toString() {
        return "User{username='" + username + "', contact='" + contact + "'}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username); // Compara pelo username
    }

    @Override
    public int hashCode() {
        return Objects.hash(username); // Hash baseado no username
    }
}