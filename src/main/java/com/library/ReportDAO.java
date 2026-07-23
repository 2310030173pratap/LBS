package com.library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ReportDAO {

    public void totalBooks() {
        printCount("SELECT COUNT(*) AS total FROM books", "total", "Total Books");
    }

    public void totalStudents() {
        printCount("SELECT COUNT(*) AS total FROM students", "total", "Total Students");
    }

    public void booksIssued() {
        printCount("SELECT COUNT(*) AS total FROM issue_books WHERE status='Issued'", "total", "Books Issued");
    }

    public void booksReturned() {
        printCount("SELECT COUNT(*) AS total FROM issue_books WHERE status='Returned'", "total", "Books Returned");
    }

    public void availableBooks() {
        printBooks("SELECT * FROM books WHERE quantity > 0", "Available Books");
    }

    public void outOfStockBooks() {
        printBooks("SELECT * FROM books WHERE quantity = 0", "Out Of Stock Books");
    }

    public void mostBorrowedBooks() {
        try {
            Connection con = DBConnection.getConnection();
            if (con == null) {
                System.out.println("Database Connection Failed");
                return;
            }

            String sql = "SELECT b.title, COUNT(*) AS borrowed FROM issue_books i JOIN books b ON b.book_id=i.book_id GROUP BY b.book_id ORDER BY borrowed DESC";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println("--------------------------------");
                System.out.println("Book : " + rs.getString("title"));
                System.out.println("Borrowed : " + rs.getInt("borrowed"));
            }

            rs.close();
            ps.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printCount(String sql, String column, String label) {
        try {
            Connection con = DBConnection.getConnection();
            if (con == null) {
                System.out.println("Database Connection Failed");
                return;
            }

            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println(label + " : " + rs.getInt(column));
            }

            rs.close();
            ps.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printBooks(String sql, String label) {
        try {
            Connection con = DBConnection.getConnection();
            if (con == null) {
                System.out.println("Database Connection Failed");
                return;
            }

            System.out.println(label);

            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println("--------------------------------");
                System.out.println("Book ID : " + rs.getInt("book_id"));
                System.out.println("Title   : " + rs.getString("title"));
                System.out.println("Author  : " + rs.getString("author"));
                System.out.println("Price   : " + rs.getDouble("price"));
                System.out.println("Qty     : " + rs.getInt("quantity"));
            }

            rs.close();
            ps.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}