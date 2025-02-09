package com.project.jobportal.entity;

import jakarta.persistence.*;

@Entity
@Table(name="recruiter_profile")
public class RecruiterProfile {

    @Id
    private Integer userAccountId;

    @OneToOne
    @JoinColumn(name = "user_account_id")
    @MapsId
    private Users usersId;

    private String city;

    private String company;

    private String country;

    private String firstName;

    private String lastName;

    @Column(nullable = true,length = 64)
    private String profilePhoto;

    private String state;

    public RecruiterProfile() {
    }

    public RecruiterProfile(Users users) {
        this.usersId = users;
    }

    public RecruiterProfile(Integer userAccountId, Users usersId, String city,
                            String company, String country, String firstName, String lastName,
                            String profilePhoto, String state) {
        this.userAccountId = userAccountId;
        this.usersId = usersId;
        this.city = city;
        this.company = company;
        this.country = country;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePhoto = profilePhoto;
        this.state = state;
    }


    public Integer getUserAccountId() {
        return userAccountId;
    }

    public void setUserAccountId(Integer userAccountId) {
        this.userAccountId = userAccountId;
    }

    public Users getUsersId() {
        return usersId;
    }

    public void setUsersId(Users usersId) {
        this.usersId = usersId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Transient
    public String getPhotosImagePath(){
        if(profilePhoto == null) return null;
        return "/photos/recruiter/"+userAccountId+"/"+profilePhoto;
    }

    @Override
    public String toString() {
        return "RecruiterProfile{" +
                "userAccountId=" + userAccountId +
                ", usersId=" + usersId +
                ", city='" + city + '\'' +
                ", company='" + company + '\'' +
                ", country='" + country + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", profilePhoto='" + profilePhoto + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
