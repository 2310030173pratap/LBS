package com.library;

public class Validation {

    public static boolean validatePhone(String phone) {
        return phone != null && phone.matches("\\d{10}");
    }

    public static boolean validatePassword(String password) {
        return password != null && password.length() >= 8;
    }

    public static boolean validatePrice(double price) {
        return price > 0;
    }

    public static boolean validateQuantity(int quantity) {
        return quantity >= 0;
    }

    public static boolean validateBookId(int bookId) {
        return bookId > 0;
    }

    public static boolean validateStudentId(int studentId) {
        return studentId > 0;
    }

    public static boolean validateUsername(String username) {
        return username != null && username.trim().length() >= 4;
    }
}