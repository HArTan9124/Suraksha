package com.example.womensafety;

public class UserData {
    private String name;
    private String gender;
    private String contactNumber;
    private String dob;
    private String description;
    private String profilePicture;

    // Default constructor required for calls to DataSnapshot.getValue(UserData.class)
    public UserData() { }

    public String getName() {
        return name;
    }
    public String getGender() {
        return gender;
    }
    public String getContactNumber() {
        return contactNumber;
    }
    public String getDob() {
        return dob;
    }
    public String getDescription() {
        return description;
    }
    public String getProfilePicture() {
        return profilePicture;
    }
}

