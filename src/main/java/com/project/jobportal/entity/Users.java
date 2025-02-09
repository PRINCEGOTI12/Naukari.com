package com.project.jobportal.entity;

import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int userId;

    @Column(unique = true)
    private String email;

    private boolean isActive;

    @NotNull
    private String password;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date registrationDate;

    @ManyToOne(cascade =CascadeType.ALL)
    @JoinColumn(name="userTypeId", referencedColumnName = "userTypeId")
    private UserType userTypeId;

    public Users(){

    }

    public Users(int id, String email, boolean isActive, String password, Date registration_date, UserType userTypeId) {
        this.userId = id;
        this.email = email;
        this.isActive = isActive;
        this.password = password;
        this.registrationDate = registration_date;
        this.userTypeId = userTypeId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int id) {
        this.userId = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getRegistration_date() {
        return registrationDate;
    }

    public void setRegistration_date(Date registration_date) {
        this.registrationDate = registration_date;
    }

    public UserType getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(UserType userTypeId) {
        this.userTypeId = userTypeId;
    }

    @Override
    public String toString() {
        return "Users{" +
                "id=" + userId +
                ", email='" + email + '\'' +
                ", isActive=" + isActive +
                ", password='" + password + '\'' +
                ", registration_date=" + registrationDate +
                ", userTypeId=" + userTypeId +
                '}';
    }
}
