package com.library;

public class Student {

	private int studentId;
	private String studentName;
	private String department;
	private int year;
	private String phone;
	private String password;

	public Student() {
	}

	public Student(int studentId, String studentName, String department, int year, String phone, String password) {
		this.studentId = studentId;
		this.studentName = studentName;
		this.department = department;
		this.year = year;
		this.phone = phone;
		this.password = password;
	}

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "Student{" +
				"studentId=" + studentId +
				", studentName='" + studentName + '\'' +
				", department='" + department + '\'' +
				", year=" + year +
				", phone='" + phone + '\'' +
				'}';
	}
}