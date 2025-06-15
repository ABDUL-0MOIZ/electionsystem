/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

/**
 *
 * @author j
 */
public class User {
    private int userId;
    private String name;
    private String cnic;
    private String password;

    // Constructor
    public User() {
    }

    public User(int userId, String name, String cnic, String password) {
        this.userId = userId;
        this.name = name;
        this.cnic = cnic;
        this.password = password;
    }

    // Getters
    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getCnic() {
        return cnic;
    }

    public String getPassword() {
        return password;
    }

    // Setters
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // toString method
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", cnic='" + cnic + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
    
}
