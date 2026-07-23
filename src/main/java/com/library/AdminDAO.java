package com.library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.library.exceptions.InvalidLoginException;

public class AdminDAO {

    public boolean loginAdmin(String username, String password) {

        if (username == null || username.trim().isEmpty()) {
            System.out.println("Empty Username");
            return false;
        }

        if (password == null || password.trim().isEmpty()) {
            System.out.println("Empty Password");
            return false;
        }

        if (username.trim().length() < 4) {
            System.out.println("Username Must Be At Least 4 Characters");
            return false;
        }

        if (password.length() < 8) {
            System.out.println("Password Must Be At Least 8 Characters");
            return false;
        }

        try {
            Connection con = DBConnection.getConnection();

            if (con == null) {
                System.out.println("Database Connection Failed");
                return false;
            }

            String sql = "SELECT * FROM admin WHERE username=? AND password=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username.trim());
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("Login Successful");
                LoggerUtil.logInfo("Admin login successful: " + username.trim());
                rs.close();
                ps.close();
                con.close();
                return true;
            }

            rs.close();
            ps.close();
            con.close();

            throw new InvalidLoginException("Invalid Username or Password");

        } catch (InvalidLoginException e) {
            System.out.println(e.getMessage());
            LoggerUtil.logError("Admin login failed for username: " + username, e);
        } catch (Exception e) {
            LoggerUtil.logError("Admin login error", e);
            e.printStackTrace();
        }

        return false;
    }

    public void changePassword(String username, String newPassword) {

        if (username == null || username.trim().isEmpty()) {
            System.out.println("Empty Username");
            return;
        }

        if (newPassword == null || newPassword.trim().isEmpty()) {
            System.out.println("Empty Password");
            return;
        }

        if (newPassword.length() < 8) {
            System.out.println("Password Must Be At Least 8 Characters");
            return;
        }

        try {
            Connection con = DBConnection.getConnection();

            if (con == null) {
                System.out.println("Database Connection Failed");
                return;
            }

            String sql = "UPDATE admin SET password=? WHERE username=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, newPassword);
            ps.setString(2, username.trim());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Password Updated Successfully");
                LoggerUtil.logInfo("Admin password changed for username: " + username.trim());
            } else {
                System.out.println("Wrong Username");
            }

            ps.close();
            con.close();

        } catch (Exception e) {
            LoggerUtil.logError("Admin change password error", e);
            e.printStackTrace();
        }
    }

    public void viewAdmin() {

        try {
            Connection con = DBConnection.getConnection();

            if (con == null) {
                System.out.println("Database Connection Failed");
                return;
            }

            String sql = "SELECT admin_id, username FROM admin";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println("--------------------------------");
                System.out.println("Admin ID : " + rs.getInt("admin_id"));
                System.out.println("Username  : " + rs.getString("username"));
            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}