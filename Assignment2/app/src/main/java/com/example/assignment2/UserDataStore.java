package com.example.assignment2;

import java.util.ArrayList;

public class UserDataStore {
    private static ArrayList<User> users = new ArrayList<>();
    private static int idCounter = 1;

    public static void addUser(User user) {
        users.add(user);
    }

    public static ArrayList<User> getUsers() {
        return users;
    }

    public static int getNextId() {
        return idCounter++;
    }
}