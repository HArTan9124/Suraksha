package com.example.womensafety;

public class UserData {
    public String name;
    public String gender;
    public String contactNumber;
    public String dob;
    public String description;
    public String profilePicture;
    public boolean id;
    public UserData(String name, String gender, String contactNumber, String dob, String description, String profilePicture, String uid) {
        this.name = name;
        this.gender = gender;
        this.contactNumber = contactNumber;
        this.dob = dob;
        this.description = description;
        this.profilePicture = profilePicture;
    }

    public String getName() {
        return name;
    }

}