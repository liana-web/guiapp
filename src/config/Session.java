/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

public class Session {
    private static int userId;
    private static String firstname;
    private static String lastname;
    private static String status;
    private static String email;
    private static String userType;

    public static void setUser(int id, String userFirstname, String userLastname, String userStatus, String userEmail, String type) {
        userId = id;
        firstname = userFirstname;
        lastname = userLastname;
        status = userStatus;
        email = userEmail;
        userType = type;
    }

    public static int getUserId() {
        return userId;
    }
    
    public static String getUserFirstname() {
        return firstname;
    }
    
    public static String getUserLastname() {
        return lastname;
    }
    
    public static String getUserStatus() {
        return status;
    }

    public static String getEmail() {
        return email;
    }

    public static String getUserType() {
        return userType;
    }

    public static void logout() {
        userId = 0;
        email = null;
        firstname = null;
        lastname = null;
        status = null;
        userType = null;
    }
}
