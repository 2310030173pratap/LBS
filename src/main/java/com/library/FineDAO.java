package com.library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.library.exceptions.FineNotFoundException;

public class FineDAO {

    public void addFine(Fine fine) {

        if (fine == null) {
            System.out.println("Invalid Fine Data");
            return;
        }

        try {
            Connection con = DBConnection.getConnection();

            if (con == null) {
                System.out.println("Database Connection Failed");
                LoggerUtil.logError("Fine insert failed: database connection unavailable", null);
                return;
            }

            String existsSql = "SELECT 1 FROM fine WHERE issue_id=?";
            PreparedStatement existsStatement = con.prepareStatement(existsSql);
            existsStatement.setInt(1, fine.getIssueId());
            ResultSet existsResultSet = existsStatement.executeQuery();

            if (existsResultSet.next()) {
                System.out.println("Fine Already Recorded");
                existsResultSet.close();
                existsStatement.close();
                con.close();
                return;
            }

            existsResultSet.close();
            existsStatement.close();

            String sql = "INSERT INTO fine(issue_id,student_id,days_late,amount,paid) VALUES(?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, fine.getIssueId());
            ps.setInt(2, fine.getStudentId());
            ps.setInt(3, fine.getDaysLate());
            ps.setDouble(4, fine.getAmount());
            ps.setString(5, fine.getPaid());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Fine Added Successfully");
                LoggerUtil.logInfo("Fine added for issue " + fine.getIssueId());
            }

            ps.close();
            con.close();

        } catch (Exception e) {
            LoggerUtil.logError("Fine insert failed", e);
            e.printStackTrace();
        }
    }

    public int calculateFine(int issueId) {

        try {
            Connection con = DBConnection.getConnection();

            if (con == null) {
                System.out.println("Database Connection Failed");
                return 0;
            }

            String sql = "SELECT student_id, DATEDIFF(return_date, due_date) AS delay_days FROM issue_books WHERE issue_id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, issueId);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                rs.close();
                ps.close();
                con.close();
                System.out.println("Fine Not Found");
                return 0;
            }

            int studentId = rs.getInt("student_id");
            int daysLate = rs.getInt("delay_days");
            int safeDaysLate = Math.max(daysLate, 0);
            double amount = safeDaysLate * 5.0;

            System.out.println("Days Late : " + safeDaysLate);
            System.out.println("Fine : ₹" + amount);

            if (amount > 0) {
                Fine fine = new Fine(0, issueId, studentId, safeDaysLate, amount, "NO");
                addFine(fine);
            }

            rs.close();
            ps.close();
            con.close();

            return (int) amount;

        } catch (Exception e) {
            LoggerUtil.logError("Fine calculation failed", e);
            e.printStackTrace();
            return 0;
        }
    }

    public void viewFineByStudent(int studentId) {

        try {
            Connection con = DBConnection.getConnection();

            if (con == null) {
                System.out.println("Database Connection Failed");
                return;
            }

            String sql = "SELECT fine_id, student_id, days_late, amount, paid FROM fine WHERE student_id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("--------------------------------");
                System.out.println("Fine ID : " + rs.getInt("fine_id"));
                System.out.println("Student ID : " + rs.getInt("student_id"));
                System.out.println("Days Late : " + rs.getInt("days_late"));
                System.out.println("Amount : ₹" + rs.getDouble("amount"));
                System.out.println("Paid : " + rs.getString("paid"));
            }

            if (!found) {
                System.out.println("No Fine Found");
            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {
            LoggerUtil.logError("View fine by student failed", e);
            e.printStackTrace();
        }
    }

    public void viewAllFine() {

        try {
            Connection con = DBConnection.getConnection();

            if (con == null) {
                System.out.println("Database Connection Failed");
                return;
            }

            String sql = "SELECT fine_id, student_id, days_late, amount, paid FROM fine";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println("--------------------------------");
                System.out.println("Fine ID : " + rs.getInt("fine_id"));
                System.out.println("Student ID : " + rs.getInt("student_id"));
                System.out.println("Days Late : " + rs.getInt("days_late"));
                System.out.println("Amount : ₹" + rs.getDouble("amount"));
                System.out.println("Paid : " + rs.getString("paid"));
            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {
            LoggerUtil.logError("View all fine failed", e);
            e.printStackTrace();
        }
    }

    public void payFine(int fineId) {

        try {
            Connection con = DBConnection.getConnection();

            if (con == null) {
                System.out.println("Database Connection Failed");
                return;
            }

            String findSql = "SELECT paid FROM fine WHERE fine_id=?";
            PreparedStatement findStatement = con.prepareStatement(findSql);
            findStatement.setInt(1, fineId);
            ResultSet rs = findStatement.executeQuery();

            if (!rs.next()) {
                rs.close();
                findStatement.close();
                con.close();
                throw new FineNotFoundException("Fine Not Found");
            }

            if ("YES".equalsIgnoreCase(rs.getString("paid"))) {
                System.out.println("Fine Already Paid");
                rs.close();
                findStatement.close();
                con.close();
                return;
            }

            rs.close();
            findStatement.close();

            String sql = "UPDATE fine SET paid='YES' WHERE fine_id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, fineId);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Fine Paid Successfully");
                LoggerUtil.logInfo("Fine paid: " + fineId);
            }

            ps.close();
            con.close();

        } catch (FineNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            LoggerUtil.logError("Pay fine failed", e);
            e.printStackTrace();
        }
    }

    public void totalFineCollected() {
        printSum("SELECT COALESCE(SUM(amount),0) AS total_amount FROM fine WHERE paid='YES'", "total_amount", "Total Fine Collected");
    }

    public void pendingFine() {
        printSum("SELECT COALESCE(SUM(amount),0) AS total_amount FROM fine WHERE paid='NO'", "total_amount", "Pending Fine");
    }

    private void printSum(String sql, String column, String label) {

        try {
            Connection con = DBConnection.getConnection();

            if (con == null) {
                System.out.println("Database Connection Failed");
                return;
            }

            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println(label + " : ₹" + rs.getDouble(column));
            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {
            LoggerUtil.logError("Fine report failed", e);
            e.printStackTrace();
        }
    }
}