package com.example.sekercan.sharingbookapplication;

/**
 * Created by sekercan on 01.05.2017.
 */

public class LoggedInUser {
    public static int id;
    public static String firstName;
    public static String lastName;

    public static void logout() {
        LoggedInUser.id = 0;
        LoggedInUser.firstName = "";
        LoggedInUser.lastName = "";
    }
}
