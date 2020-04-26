package com.example.djigitteamsofia;

public class SignUpData {
    String UserName, UserEmail, UserPassword;

    public SignUpData(String userName, String userEmail, String userPassword) {
        UserName = userName;
        UserEmail = userEmail;
        UserPassword = userPassword;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserEmail() {
        return UserEmail;
    }

    public void setUserEmail(String userEmail) {
        UserEmail = userEmail;
    }
    public String getUserPassword() {
        return UserPassword;
    }

    public void setUserPassword(String userPassword) {
        UserPassword = userPassword;
    }

}
