package com.library;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu {

	public static void showMenu() {

		Scanner sc = new Scanner(System.in);

		BookDAO dao = new BookDAO();
		StudentDAO studentDAO = new StudentDAO();
		AdminDAO adminDAO = new AdminDAO();
		IssueBookDAO issueBookDAO = new IssueBookDAO();
		ReportDAO reportDAO = new ReportDAO();
		FineDAO fineDAO = new FineDAO();

		while (true) {
			System.out.println("============= LIBRARY =============");
			System.out.println("1 Book Management");
			System.out.println("2 Student Management");
			System.out.println("3 Admin Login");
			System.out.println("4 Exit");
			System.out.println("==================================");
			System.out.print("Enter Choice: ");

			int choice = readInt(sc);

			switch(choice) {
			case 1:
				showBookMenu(sc, dao);
				break;
			case 2:
				showStudentMenu(sc, studentDAO);
				break;
			case 3:
				showAdminLogin(sc, adminDAO, dao, studentDAO, issueBookDAO, reportDAO, fineDAO);
				break;
			case 4:
				System.exit(0);
			default:
				System.out.println("Invalid Choice");
			}
		}
	}

	private static int readInt(Scanner sc) {
		while (true) {
			try {
				return sc.nextInt();
			} catch (InputMismatchException e) {
				System.out.println("Invalid menu input");
				sc.nextLine();
				System.out.print("Enter Choice: ");
			}
		}
	}

	private static void showBookMenu(Scanner sc, BookDAO dao) {
		System.out.println("============= LIBRARY =============");
		System.out.println();
		System.out.println("1 Add Book");
		System.out.println();
		System.out.println("2 Search Book");
		System.out.println();
		System.out.println("3 View All Books");
		System.out.println();
		System.out.println("4 Update Price");
		System.out.println();
		System.out.println("5 Update Quantity");
		System.out.println();
		System.out.println("6 Delete Book");
		System.out.println();
		System.out.println("7 Exit");
		System.out.println();
		System.out.println("==================================");

		System.out.print("Enter Choice: ");

		int choice = readInt(sc);

		switch(choice){

		case 1:

			Book book = new Book();

			System.out.print("Book ID: ");
			book.setBookId(sc.nextInt());

			sc.nextLine();

			System.out.print("Title: ");
			book.setTitle(sc.nextLine());

			System.out.print("Author: ");
			book.setAuthor(sc.nextLine());

			System.out.print("Category: ");
			book.setCategory(sc.nextLine());

			System.out.print("Price: ");
			book.setPrice(sc.nextDouble());

			System.out.print("Quantity: ");
			book.setQuantity(sc.nextInt());

			dao.addBook(book);

			break;

		case 2:

			sc.nextLine();

			System.out.print("Enter Book Title: ");

			String title = sc.nextLine();

			dao.searchBook(title);

			break;

		case 3:
			dao.viewAllBooks();
			break;

		case 4:

			System.out.print("Book ID : ");

			int id=sc.nextInt();

			System.out.print("New Price : ");

			double price=sc.nextDouble();

			dao.updateBookPrice(id,price);

			break;

		case 5:

			System.out.print("Book ID : ");

			id=sc.nextInt();

			System.out.print("New Quantity : ");

			int qty=sc.nextInt();

			dao.updateBookQuantity(id,qty);

			break;

		case 6:

			System.out.print("Book ID : ");

			id=sc.nextInt();

			dao.deleteBook(id);

			break;

		case 7:

			System.exit(0);

		default:
			System.out.println("Invalid Choice");
		}
	}

	private static void showStudentMenu(Scanner sc, StudentDAO studentDAO) {

		while (true) {
			System.out.println("========== STUDENT ==========");
			System.out.println("1 Register");
			System.out.println("2 Login");
			System.out.println("3 View Profile");
			System.out.println("4 Update Profile");
			System.out.println("5 Delete Profile");
			System.out.println("6 View All Students");
			System.out.println("7 Back");
			System.out.println("=============================");

			System.out.print("Enter Choice: ");

			int choice = readInt(sc);

			switch (choice) {
			case 1:
				Student student = new Student();

				System.out.print("Student ID: ");
				student.setStudentId(readInt(sc));

				sc.nextLine();

				System.out.print("Name: ");
				student.setStudentName(sc.nextLine());

				System.out.print("Department: ");
				student.setDepartment(sc.nextLine());

				System.out.print("Year: ");
				student.setYear(readInt(sc));

				sc.nextLine();

				System.out.print("Phone: ");
				student.setPhone(sc.nextLine());

				System.out.print("Password: ");
				student.setPassword(sc.nextLine());

				studentDAO.registerStudent(student);
				break;
			case 2:
				System.out.print("Student ID: ");
				int studentId = readInt(sc);
				sc.nextLine();
				System.out.print("Password: ");
				String password = sc.nextLine();
				studentDAO.loginStudent(studentId, password);
				break;
			case 3:
				System.out.print("Student ID: ");
				studentId = readInt(sc);
				studentDAO.viewProfile(studentId);
				break;
			case 4:
				Student updateStudent = new Student();

				System.out.print("Student ID: ");
				updateStudent.setStudentId(readInt(sc));

				sc.nextLine();

				System.out.print("Name: ");
				updateStudent.setStudentName(sc.nextLine());

				System.out.print("Department: ");
				updateStudent.setDepartment(sc.nextLine());

				System.out.print("Year: ");
				updateStudent.setYear(readInt(sc));

				sc.nextLine();

				System.out.print("Phone: ");
				updateStudent.setPhone(sc.nextLine());

				System.out.print("Password: ");
				updateStudent.setPassword(sc.nextLine());

				studentDAO.updateProfile(updateStudent);
				break;
			case 5:
				System.out.print("Student ID: ");
				studentId = readInt(sc);
				studentDAO.deleteStudent(studentId);
				break;
			case 6:
				studentDAO.viewAllStudents();
				break;
			case 7:
				return;
			default:
				System.out.println("Invalid Choice");
			}
		}

	}

	private static void showAdminLogin(Scanner sc, AdminDAO adminDAO, BookDAO bookDAO, StudentDAO studentDAO,
			IssueBookDAO issueBookDAO, ReportDAO reportDAO, FineDAO fineDAO) {

		System.out.println("Welcome Admin");
		System.out.print("Username: ");
		sc.nextLine();
		String username = sc.nextLine();
		System.out.print("Password: ");
		String password = sc.nextLine();

		if (adminDAO.loginAdmin(username, password)) {
			showAdminDashboard(sc, adminDAO, bookDAO, studentDAO, issueBookDAO, reportDAO, fineDAO, username);
		}
	}

	private static void showAdminDashboard(Scanner sc, AdminDAO adminDAO, BookDAO bookDAO, StudentDAO studentDAO,
			IssueBookDAO issueBookDAO, ReportDAO reportDAO, FineDAO fineDAO, String username) {

		while (true) {
			System.out.println("===========================");
			System.out.println("      ADMIN DASHBOARD");
			System.out.println("===========================");
			System.out.println();
			System.out.println("1. Add Book");
			System.out.println("2. Search Book");
			System.out.println("3. View Books");
			System.out.println("4. Update Book");
			System.out.println("5. Delete Book");
			System.out.println();
			System.out.println("6. Register Student");
			System.out.println("7. View Students");
			System.out.println();
			System.out.println("8. Issue Book");
			System.out.println("9. Return Book");
			System.out.println();
			System.out.println("10. Reports");
			System.out.println();
			System.out.println("11. Change Password");
			System.out.println();
			System.out.println("12. Fine Management");
			System.out.println();
			System.out.println("13. Logout");

			System.out.print("Enter Choice: ");
			int choice = readInt(sc);

			switch (choice) {
			case 1:
				addBookFlow(sc, bookDAO);
				break;
			case 2:
				searchBookFlow(sc, bookDAO);
				break;
			case 3:
				bookDAO.viewAllBooks();
				break;
			case 4:
				updateBookFlow(sc, bookDAO);
				break;
			case 5:
				deleteBookFlow(sc, bookDAO);
				break;
			case 6:
				registerStudentFlow(sc, studentDAO);
				break;
			case 7:
				studentDAO.viewAllStudents();
				break;
			case 8:
				issueBookFlow(sc, issueBookDAO);
				break;
			case 9:
				returnBookFlow(sc, issueBookDAO);
				break;
			case 10:
				showReportsMenu(sc, reportDAO);
				break;
			case 11:
				changePasswordFlow(sc, adminDAO, username);
				break;
			case 12:
				showFineMenu(sc, fineDAO);
				break;
			case 13:
				return;
			default:
				System.out.println("Invalid Choice");
			}
		}
	}

	private static void addBookFlow(Scanner sc, BookDAO dao) {
		Book book = new Book();
		System.out.print("Book ID: ");
		book.setBookId(readInt(sc));
		sc.nextLine();
		System.out.print("Title: ");
		book.setTitle(sc.nextLine());
		System.out.print("Author: ");
		book.setAuthor(sc.nextLine());
		System.out.print("Category: ");
		book.setCategory(sc.nextLine());
		System.out.print("Price: ");
		book.setPrice(sc.nextDouble());
		System.out.print("Quantity: ");
		book.setQuantity(readInt(sc));
		dao.addBook(book);
	}

	private static void searchBookFlow(Scanner sc, BookDAO dao) {
		sc.nextLine();
		System.out.print("Enter Book Title: ");
		String title = sc.nextLine();
		dao.searchBook(title);
	}

	private static void updateBookFlow(Scanner sc, BookDAO dao) {
		System.out.print("Book ID : ");
		int id = readInt(sc);
		System.out.print("1 Update Price\n2 Update Quantity\nEnter Choice: ");
		int choice = readInt(sc);
		if (choice == 1) {
			System.out.print("New Price : ");
			double price = sc.nextDouble();
			dao.updateBookPrice(id, price);
		} else if (choice == 2) {
			System.out.print("New Quantity : ");
			int qty = readInt(sc);
			dao.updateBookQuantity(id, qty);
		} else {
			System.out.println("Invalid Choice");
		}
	}

	private static void deleteBookFlow(Scanner sc, BookDAO dao) {
		System.out.print("Book ID : ");
		int id = readInt(sc);
		dao.deleteBook(id);
	}

	private static void registerStudentFlow(Scanner sc, StudentDAO studentDAO) {
		Student student = new Student();
		System.out.print("Student ID: ");
		student.setStudentId(readInt(sc));
		sc.nextLine();
		System.out.print("Name: ");
		student.setStudentName(sc.nextLine());
		System.out.print("Department: ");
		student.setDepartment(sc.nextLine());
		System.out.print("Year: ");
		student.setYear(readInt(sc));
		sc.nextLine();
		System.out.print("Phone: ");
		student.setPhone(sc.nextLine());
		System.out.print("Password: ");
		student.setPassword(sc.nextLine());
		studentDAO.registerStudent(student);
	}

	private static void issueBookFlow(Scanner sc, IssueBookDAO issueBookDAO) {
		System.out.print("Book ID: ");
		int bookId = readInt(sc);
		System.out.print("Student ID: ");
		int studentId = readInt(sc);
		issueBookDAO.issueBook(bookId, studentId);
	}

	private static void returnBookFlow(Scanner sc, IssueBookDAO issueBookDAO) {
		System.out.print("Issue ID: ");
		int issueId = readInt(sc);
		issueBookDAO.returnBook(issueId);
	}

	private static void showReportsMenu(Scanner sc, ReportDAO reportDAO) {
		while (true) {
			System.out.println("========== REPORTS ==========");
			System.out.println();
			System.out.println("1. Total Books");
			System.out.println();
			System.out.println("2. Total Students");
			System.out.println();
			System.out.println("3. Books Issued");
			System.out.println();
			System.out.println("4. Books Returned");
			System.out.println();
			System.out.println("5. Available Books");
			System.out.println();
			System.out.println("6. Out of Stock Books");
			System.out.println();
			System.out.println("7. Most Borrowed Books");
			System.out.println();
			System.out.println("8. Exit");
			System.out.print("Enter Choice: ");

			int choice = readInt(sc);
			switch (choice) {
			case 1:
				reportDAO.totalBooks();
				break;
			case 2:
				reportDAO.totalStudents();
				break;
			case 3:
				reportDAO.booksIssued();
				break;
			case 4:
				reportDAO.booksReturned();
				break;
			case 5:
				reportDAO.availableBooks();
				break;
			case 6:
				reportDAO.outOfStockBooks();
				break;
			case 7:
				reportDAO.mostBorrowedBooks();
				break;
			case 8:
				return;
			default:
				System.out.println("Invalid Choice");
			}
		}
	}

	private static void showFineMenu(Scanner sc, FineDAO fineDAO) {
		while (true) {
			System.out.println("========== FINE =========");
			System.out.println();
			System.out.println("1 Calculate Fine");
			System.out.println();
			System.out.println("2 View Fine");
			System.out.println();
			System.out.println("3 Pay Fine");
			System.out.println();
			System.out.println("4 Fine Report");
			System.out.println();
			System.out.println("5 Back");
			System.out.print("Enter Choice: ");

			int choice = readInt(sc);
			switch (choice) {
			case 1:
				System.out.print("Issue ID: ");
				int issueId = readInt(sc);
				if (!Validation.validateBookId(issueId)) {
					System.out.println("Invalid Issue ID");
					break;
				}
				fineDAO.calculateFine(issueId);
				break;
			case 2:
				System.out.print("Student ID: ");
				int studentId = readInt(sc);
				if (!Validation.validateStudentId(studentId)) {
					System.out.println("Invalid Student ID");
					break;
				}
				fineDAO.viewFineByStudent(studentId);
				break;
			case 3:
				System.out.print("Fine ID: ");
				int fineId = readInt(sc);
				if (fineId <= 0) {
					System.out.println("Invalid Fine ID");
					break;
				}
				fineDAO.payFine(fineId);
				break;
			case 4:
				showFineReportMenu(sc, fineDAO);
				break;
			case 5:
				return;
			default:
				System.out.println("Invalid Choice");
			}
		}
	}

	private static void showFineReportMenu(Scanner sc, FineDAO fineDAO) {
		while (true) {
			System.out.println("========== FINE REPORT ==========");
			System.out.println("1. Total Fine Collected");
			System.out.println("2. Pending Fine");
			System.out.println("3. Back");
			System.out.print("Enter Choice: ");

			int choice = readInt(sc);
			switch (choice) {
			case 1:
				fineDAO.totalFineCollected();
				break;
			case 2:
				fineDAO.pendingFine();
				break;
			case 3:
				return;
			default:
				System.out.println("Invalid Choice");
			}
		}
	}

	private static void changePasswordFlow(Scanner sc, AdminDAO adminDAO, String username) {
		sc.nextLine();
		System.out.print("New Password: ");
		String newPassword = sc.nextLine();
		adminDAO.changePassword(username, newPassword);
	}
}