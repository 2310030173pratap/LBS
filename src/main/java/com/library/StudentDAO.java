package com.library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;

public class StudentDAO {

	private boolean isBlank(String value) {
		return value == null || value.trim().isEmpty();
	}

	private boolean isValidPhone(String phone) {
		return phone != null && phone.matches("\\d{10}");
	}

	private boolean isValidYear(int year) {
		return year >= 1 && year <= 4;
	}

	public void registerStudent(Student student) {

		if (student == null) {
			System.out.println("Invalid Student Data");
			return;
		}

		if (isBlank(student.getStudentName())) {
			System.out.println("Name Cannot Be Empty");
			return;
		}

		if (isBlank(student.getPassword())) {
			System.out.println("Password Cannot Be Empty");
			return;
		}

		if (!isValidPhone(student.getPhone())) {
			System.out.println("Invalid Phone Number");
			return;
		}

		if (!isValidYear(student.getYear())) {
			System.out.println("Year Must Be Between 1 and 4");
			return;
		}

		if (student.getPassword().length() < 8) {
			System.out.println("Password Must Be At Least 8 Characters");
			return;
		}

		Connection con = null;
		PreparedStatement checkStatement = null;
		PreparedStatement insertStatement = null;
		ResultSet rs = null;

		try {
			con = DBConnection.getConnection();

			if (con == null) {
				System.out.println("Database Connection Failed");
				return;
			}

			String checkSql = "SELECT 1 FROM students WHERE student_id=?";
			checkStatement = con.prepareStatement(checkSql);
			checkStatement.setInt(1, student.getStudentId());
			rs = checkStatement.executeQuery();

			if (rs.next()) {
				System.out.println("Student Already Exists");
				return;
			}

			String insertSql = "INSERT INTO students(student_id, student_name, department, year, phone, password) VALUES(?,?,?,?,?,?)";
			insertStatement = con.prepareStatement(insertSql);
			insertStatement.setInt(1, student.getStudentId());
			insertStatement.setString(2, student.getStudentName());
			insertStatement.setString(3, student.getDepartment());
			insertStatement.setInt(4, student.getYear());
			insertStatement.setString(5, student.getPhone());
			insertStatement.setString(6, student.getPassword());

			int rows = insertStatement.executeUpdate();

			if (rows > 0) {
				System.out.println("Student Registered Successfully");
			}
		} catch (SQLIntegrityConstraintViolationException e) {
			System.out.println("Student Already Exists");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (checkStatement != null) {
					checkStatement.close();
				}
				if (insertStatement != null) {
					insertStatement.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void loginStudent(int studentId, String password) {

		try {
			Connection con = DBConnection.getConnection();

			if (con == null) {
				System.out.println("Database Connection Failed");
				return;
			}

			String sql = "SELECT * FROM students WHERE student_id=? AND password=?";

			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, studentId);
			ps.setString(2, password);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				System.out.println("Login Successful");
			} else {
				System.out.println("Invalid Student ID or Password");
			}

			rs.close();
			ps.close();
			con.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void viewProfile(int studentId) {

		try {
			Connection con = DBConnection.getConnection();

			if (con == null) {
				System.out.println("Database Connection Failed");
				return;
			}

			String sql = "SELECT * FROM students WHERE student_id=?";

			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, studentId);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				System.out.println("Student ID : " + rs.getInt("student_id"));
				System.out.println("Name       : " + rs.getString("student_name"));
				System.out.println("Department : " + rs.getString("department"));
				System.out.println("Year       : " + rs.getInt("year"));
				System.out.println("Phone      : " + rs.getString("phone"));
			} else {
				System.out.println("Student Not Found");
			}

			rs.close();
			ps.close();
			con.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateProfile(Student student) {

		if (student == null) {
			System.out.println("Invalid Student Data");
			return;
		}

		if (isBlank(student.getStudentName())) {
			System.out.println("Name Cannot Be Empty");
			return;
		}

		if (isBlank(student.getPassword())) {
			System.out.println("Password Cannot Be Empty");
			return;
		}

		if (!isValidPhone(student.getPhone())) {
			System.out.println("Invalid Phone Number");
			return;
		}

		if (!isValidYear(student.getYear())) {
			System.out.println("Year Must Be Between 1 and 4");
			return;
		}

		try {
			Connection con = DBConnection.getConnection();

			if (con == null) {
				System.out.println("Database Connection Failed");
				return;
			}

			String sql = "UPDATE students SET student_name=?, department=?, year=?, phone=?, password=? WHERE student_id=?";

			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, student.getStudentName());
			ps.setString(2, student.getDepartment());
			ps.setInt(3, student.getYear());
			ps.setString(4, student.getPhone());
			ps.setString(5, student.getPassword());
			ps.setInt(6, student.getStudentId());

			int rows = ps.executeUpdate();

			if (rows > 0) {
				System.out.println("Profile Updated Successfully");
			} else {
				System.out.println("Student Not Found");
			}

			ps.close();
			con.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteStudent(int studentId) {

		try {
			Connection con = DBConnection.getConnection();

			if (con == null) {
				System.out.println("Database Connection Failed");
				return;
			}

			String sql = "DELETE FROM students WHERE student_id=?";

			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, studentId);

			int rows = ps.executeUpdate();

			if (rows > 0) {
				System.out.println("Student Deleted Successfully");
			} else {
				System.out.println("Student Not Found");
			}

			ps.close();
			con.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void viewAllStudents() {

		try {
			Connection con = DBConnection.getConnection();

			if (con == null) {
				System.out.println("Database Connection Failed");
				return;
			}

			String sql = "SELECT * FROM students";

			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				System.out.println("--------------------------------");
				System.out.println("Student ID : " + rs.getInt("student_id"));
				System.out.println("Name       : " + rs.getString("student_name"));
				System.out.println("Department : " + rs.getString("department"));
				System.out.println("Year       : " + rs.getInt("year"));
				System.out.println("Phone      : " + rs.getString("phone"));
			}

			rs.close();
			ps.close();
			con.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}