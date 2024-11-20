package client.models;

import java.io.Serializable;

public class User implements Serializable {

    private static final long serialVersionUID = 1L; // Уникальный идентификатор версии класса

    private String username;
    private String password;
    private String role;  // Роль пользователя (client или admin)

    // Конструктор для обычного пользователя (role по умолчанию = "client")
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.role = "client";  // По умолчанию роль клиента
    }

    // Конструктор для пользователя с заданной ролью
    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
}
