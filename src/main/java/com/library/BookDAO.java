package com.library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BookDAO {

	public void addBook(Book book) {

		try {

			Connection con = DBConnection.getConnection();

			String sql =
				"INSERT INTO books(book_id,title,author,category,price,quantity) VALUES(?,?,?,?,?,?)";

			PreparedStatement ps = con.prepareStatement(sql);

			ps.setInt(1, book.getBookId());
			ps.setString(2, book.getTitle());
			ps.setString(3, book.getAuthor());
			ps.setString(4, book.getCategory());
			ps.setDouble(5, book.getPrice());
			ps.setInt(6, book.getQuantity());

			int rows = ps.executeUpdate();

			if (rows > 0) {
				System.out.println("Book Added Successfully");
			}

			ps.close();
			con.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void searchBook(String title) {

		try {

			Connection con = DBConnection.getConnection();

			String sql = "SELECT * FROM books WHERE title LIKE ?";

			PreparedStatement ps = con.prepareStatement(sql);

			ps.setString(1, "%" + title + "%");

			ResultSet rs = ps.executeQuery();

			boolean found = false;

			while (rs.next()) {

				found = true;

				System.out.println("-----------------------------");
				System.out.println("Book ID   : " + rs.getInt("book_id"));
				System.out.println("Title     : " + rs.getString("title"));
				System.out.println("Author    : " + rs.getString("author"));
				System.out.println("Category  : " + rs.getString("category"));
				System.out.println("Price     : " + rs.getDouble("price"));
				System.out.println("Quantity  : " + rs.getInt("quantity"));
			}

			if (!found) {
				System.out.println("No book found.");
			}

			rs.close();
			ps.close();
			con.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void viewAllBooks() {

		try {

			Connection con = DBConnection.getConnection();

			String sql = "SELECT * FROM books";

			PreparedStatement ps = con.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				System.out.println("--------------------------------");

				System.out.println("Book ID : " + rs.getInt("book_id"));
				System.out.println("Title : " + rs.getString("title"));
				System.out.println("Author : " + rs.getString("author"));
				System.out.println("Category : " + rs.getString("category"));
				System.out.println("Price : " + rs.getDouble("price"));
				System.out.println("Quantity : " + rs.getInt("quantity"));

			}

			rs.close();
			ps.close();
			con.close();

		} catch(Exception e) {
			e.printStackTrace();
		}

	}

	public void updateBookPrice(int id,double price){

		try{

			Connection con=DBConnection.getConnection();

			String sql="UPDATE books SET price=? WHERE book_id=?";

			PreparedStatement ps=con.prepareStatement(sql);

			ps.setDouble(1,price);

			ps.setInt(2,id);

			int rows=ps.executeUpdate();

			if(rows>0)
				System.out.println("Price Updated Successfully");
			else
				System.out.println("Book Not Found");

			ps.close();

			con.close();

		}

		catch(Exception e){

			e.printStackTrace();

		}

	}

	public void updateBookQuantity(int id,int qty){

		try{

			Connection con=DBConnection.getConnection();

			String sql="UPDATE books SET quantity=? WHERE book_id=?";

			PreparedStatement ps=con.prepareStatement(sql);

			ps.setInt(1,qty);

			ps.setInt(2,id);

			int rows=ps.executeUpdate();

			if(rows>0)
				System.out.println("Quantity Updated Successfully");
			else
				System.out.println("Book Not Found");

			ps.close();

			con.close();

		}

		catch(Exception e){

			e.printStackTrace();

		}

	}

	public void deleteBook(int id){

		try{

			Connection con=DBConnection.getConnection();

			String sql="DELETE FROM books WHERE book_id=?";

			PreparedStatement ps=con.prepareStatement(sql);

			ps.setInt(1,id);

			int rows=ps.executeUpdate();

			if(rows>0)
				System.out.println("Book Deleted Successfully");
			else
				System.out.println("Book Not Found");

			ps.close();

			con.close();

		}

		catch(Exception e){

			e.printStackTrace();

		}

	}
}