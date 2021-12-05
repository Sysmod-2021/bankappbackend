package model;

public class AuthResponse {
    String userId;
    String userType;
    AuthResponse(String userId, String userType){
        this.userId = userId;
        this.userType = userType;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserType() {
        return userType;
    }
}