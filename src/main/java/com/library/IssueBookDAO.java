package com.library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import com.library.exceptions.BookNotFoundException;
import com.library.exceptions.BookOutOfStockException;
import com.library.exceptions.StudentNotFoundException;

public class IssueBookDAO {

    public boolean checkBook(int bookId) {
        return exists("SELECT 1 FROM books WHERE book_id=?", bookId, "book_id");
    }

    public boolean checkStudent(int studentId) {
        return exists("SELECT 1 FROM students WHERE student_id=?", studentId, "student_id");
    }

    public int getBookQuantity(int bookId) {

        try {
            Connection con = DBConnection.getConnection();
            if (con == null) {
                System.out.println("Database Connection Failed");
                return -1;
            }

            String sql = "SELECT quantity FROM books WHERE book_id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, bookId);
            ResultSet rs = ps.executeQuery();

            int quantity = -1;
            if (rs.next()) {
                quantity = rs.getInt("quantity");
            }

            rs.close();
            ps.close();
            con.close();
            return quantity;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public boolean checkQuantity(int bookId) {
        return getBookQuantity(bookId) > 0;
    }

    public void updateQuantity(int bookId) {

        try {
            Connection con = DBConnection.getConnection();
            if (con == null) {
                System.out.println("Database Connection Failed");
                return;
            }

            String sql = "UPDATE books SET quantity=quantity-1 WHERE book_id=? AND quantity>0";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, bookId);

            ps.executeUpdate();
            ps.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void issueBook(int bookId, int studentId) {

        Connection con = null;
        PreparedStatement duplicateStatement = null;
        PreparedStatement insertStatement = null;
        PreparedStatement updateStatement = null;
        ResultSet duplicateRs = null;

        try {
            if (!checkBook(bookId)) {
                throw new BookNotFoundException("Book Not Found");
            }

            if (!checkStudent(studentId)) {
                throw new StudentNotFoundException("Student Not Found");
            }

            if (!checkQuantity(bookId)) {
                throw new BookOutOfStockException("Book Out Of Stock");
            }

            con = DBConnection.getConnection();
            if (con == null) {
                System.out.println("Database Connection Failed");
                return;
            }

            con.setAutoCommit(false);

            String duplicateSql = "SELECT 1 FROM issue_books WHERE book_id=? AND student_id=? AND status='Issued'";
            duplicateStatement = con.prepareStatement(duplicateSql);
            duplicateStatement.setInt(1, bookId);
            duplicateStatement.setInt(2, studentId);
            duplicateRs = duplicateStatement.executeQuery();

            if (duplicateRs.next()) {
                System.out.println("Already Issued");
                con.rollback();
                return;
            }

            String insertSql = "INSERT INTO issue_books(book_id, student_id, issue_date, due_date, status) VALUES(?,?,CURRENT_DATE(),DATE_ADD(CURRENT_DATE(),INTERVAL 7 DAY),'Issued')";
            insertStatement = con.prepareStatement(insertSql);
            insertStatement.setInt(1, bookId);
            insertStatement.setInt(2, studentId);

            int inserted = insertStatement.executeUpdate();

            if (inserted > 0) {
                String updateSql = "UPDATE books SET quantity=quantity-1 WHERE book_id=? AND quantity>0";
                updateStatement = con.prepareStatement(updateSql);
                updateStatement.setInt(1, bookId);
                int updated = updateStatement.executeUpdate();

                if (updated > 0) {
                    con.commit();
                    LocalDate issueDate = LocalDate.now();
                    LocalDate dueDate = issueDate.plusDays(7);
                    System.out.println("Book Issued Successfully");
                    System.out.println();
                    System.out.println("Issue Date");
                    System.out.println();
                    System.out.println(issueDate);
                    System.out.println();
                    System.out.println("Due Date");
                    System.out.println();
                    System.out.println(dueDate);
                    LoggerUtil.logInfo("Book issued: bookId=" + bookId + ", studentId=" + studentId);
                } else {
                    con.rollback();
                    System.out.println("Out Of Stock");
                }
            } else {
                con.rollback();
            }

        } catch (BookNotFoundException | StudentNotFoundException | BookOutOfStockException e) {
            System.out.println(e.getMessage());
            LoggerUtil.logError("Issue book validation failed", e);

        } catch (Exception e) {
            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
            }
            LoggerUtil.logError("Issue book error", e);
            e.printStackTrace();
        } finally {
            try {
                if (duplicateRs != null) {
                    duplicateRs.close();
                }
                if (duplicateStatement != null) {
                    duplicateStatement.close();
                }
                if (insertStatement != null) {
                    insertStatement.close();
                }
                if (updateStatement != null) {
                    updateStatement.close();
                }
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void viewIssuedBooks() {

        try {
            Connection con = DBConnection.getConnection();
            if (con == null) {
                System.out.println("Database Connection Failed");
                return;
            }

            String sql = "SELECT i.issue_id, s.student_name, b.title, i.issue_date, i.due_date, i.status FROM issue_books i JOIN students s ON s.student_id=i.student_id JOIN books b ON b.book_id=i.book_id";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println("Issue ID : " + rs.getInt("issue_id"));
                System.out.println();
                System.out.println("Student : " + rs.getString("student_name"));
                System.out.println();
                System.out.println("Book : " + rs.getString("title"));
                System.out.println();
                System.out.println("Issue : " + rs.getDate("issue_date"));
                System.out.println();
                System.out.println("Due : " + rs.getDate("due_date"));
                System.out.println();
                System.out.println("Status : " + rs.getString("status"));
                System.out.println();
            }

            rs.close();
            ps.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void viewStudentIssuedBooks(int studentId) {

        try {
            Connection con = DBConnection.getConnection();
            if (con == null) {
                System.out.println("Database Connection Failed");
                return;
            }

            String sql = "SELECT b.title, i.issue_date, i.status FROM issue_books i JOIN books b ON b.book_id=i.book_id WHERE i.student_id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("Book : " + rs.getString("title"));
                System.out.println();
                System.out.println("Issue : " + rs.getDate("issue_date"));
                System.out.println();
                System.out.println("Status : " + rs.getString("status"));
                System.out.println();
            }

            if (!found) {
                System.out.println("No Records Found");
            }

            rs.close();
            ps.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean getIssueDetails(int issueId) {
        try {
            Connection con = DBConnection.getConnection();
            if (con == null) {
                System.out.println("Database Connection Failed");
                return false;
            }

            String sql = "SELECT issue_id, book_id, student_id, issue_date, due_date, return_date, status FROM issue_books WHERE issue_id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, issueId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("Issue ID : " + rs.getInt("issue_id"));
                System.out.println("Book ID : " + rs.getInt("book_id"));
                System.out.println("Student ID : " + rs.getInt("student_id"));
                System.out.println("Issue Date : " + rs.getDate("issue_date"));
                System.out.println("Due Date : " + rs.getDate("due_date"));
                System.out.println("Return Date : " + rs.getDate("return_date"));
                System.out.println("Status : " + rs.getString("status"));
                rs.close();
                ps.close();
                con.close();
                return true;
            }

            rs.close();
            ps.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public int calculateFine(int issueId) {
        int fine = 0;
        try {
            Connection con = DBConnection.getConnection();
            if (con == null) {
                System.out.println("Database Connection Failed");
                return 0;
            }

            String sql = "SELECT DATEDIFF(return_date,due_date) AS delay_days FROM issue_books WHERE issue_id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, issueId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int delay = rs.getInt("delay_days");
                if (delay > 0) {
                    fine = delay * 5;
                }
            }

            rs.close();
            ps.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fine;
    }

    public void updateBookQuantityAfterReturn(int bookId) {
        try {
            Connection con = DBConnection.getConnection();
            if (con == null) {
                System.out.println("Database Connection Failed");
                return;
            }

            String sql = "UPDATE books SET quantity = quantity + 1 WHERE book_id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, bookId);
            ps.executeUpdate();
            ps.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void returnBook(int issueId) {

        Connection con = null;
        PreparedStatement checkStatement = null;
        PreparedStatement updateStatement = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getConnection();
            if (con == null) {
                System.out.println("Database Connection Failed");
                return;
            }

            con.setAutoCommit(false);

            String checkSql = "SELECT book_id, student_id, due_date, status FROM issue_books WHERE issue_id=?";
            checkStatement = con.prepareStatement(checkSql);
            checkStatement.setInt(1, issueId);
            rs = checkStatement.executeQuery();

            if (!rs.next()) {
                System.out.println("Issue ID Not Found");
                con.rollback();
                return;
            }

            int bookId = rs.getInt("book_id");
            int studentId = rs.getInt("student_id");
            String status = rs.getString("status");
            LocalDate dueDate = rs.getDate("due_date").toLocalDate();

            if ("Returned".equalsIgnoreCase(status)) {
                System.out.println("Already Returned");
                con.rollback();
                return;
            }

            String updateSql = "UPDATE issue_books SET return_date=CURRENT_DATE(), status='Returned' WHERE issue_id=?";
            updateStatement = con.prepareStatement(updateSql);
            updateStatement.setInt(1, issueId);
            int rows = updateStatement.executeUpdate();

            if (rows > 0) {
                updateBookQuantityAfterReturn(bookId);
                con.commit();

                LocalDate returnDate = LocalDate.now();
                int delay = (int) ChronoUnit.DAYS.between(dueDate, returnDate);
                int fine = Math.max(delay, 0) * 5;

                if (fine > 0) {
                    FineDAO fineDAO = new FineDAO();
                    Fine fineRecord = new Fine(0, issueId, studentId, Math.max(delay, 0), fine, "NO");
                    fineDAO.addFine(fineRecord);
                }

                System.out.println("Book Returned Successfully");
                System.out.println();
                System.out.println("Return Date : " + returnDate);
                System.out.println();
                System.out.println("Delay : " + Math.max(delay, 0) + " Days");
                System.out.println();
                System.out.println("Fine : ₹" + fine);
                LoggerUtil.logInfo("Book returned: issueId=" + issueId + ", fine=" + fine);
            } else {
                con.rollback();
                System.out.println("Database Error");
            }

        } catch (Exception e) {
            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
            }
            LoggerUtil.logError("Return book error", e);
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (checkStatement != null) {
                    checkStatement.close();
                }
                if (updateStatement != null) {
                    updateStatement.close();
                }
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void viewReturnedBooks() {

        try {
            Connection con = DBConnection.getConnection();
            if (con == null) {
                System.out.println("Database Connection Failed");
                return;
            }

            String sql = "SELECT issue_id, book_id, student_id, return_date, status FROM issue_books WHERE status='Returned'";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println("--------------------------------");
                System.out.println("Issue ID : " + rs.getInt("issue_id"));
                System.out.println("Book ID : " + rs.getInt("book_id"));
                System.out.println("Student ID : " + rs.getInt("student_id"));
                System.out.println("Return Date : " + rs.getDate("return_date"));
                System.out.println("Status : " + rs.getString("status"));
            }

            rs.close();
            ps.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean exists(String sql, int id, String column) {
        try {
            Connection con = DBConnection.getConnection();
            if (con == null) {
                System.out.println("Database Connection Failed");
                return false;
            }

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            boolean found = rs.next();
            rs.close();
            ps.close();
            con.close();
            return found;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}