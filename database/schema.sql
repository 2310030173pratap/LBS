CREATE DATABASE IF NOT EXISTS library;
USE library;

CREATE TABLE IF NOT EXISTS books (
    book_id INT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    author VARCHAR(150) NOT NULL,
    category VARCHAR(100) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    quantity INT NOT NULL
);

CREATE TABLE IF NOT EXISTS students (
    student_id INT PRIMARY KEY,
    student_name VARCHAR(150) NOT NULL,
    department VARCHAR(100) NOT NULL,
    year INT NOT NULL,
    phone VARCHAR(15) NOT NULL,
    password VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS admin (
    admin_id INT PRIMARY KEY,
    username VARCHAR(50) UNIQUE,
    password VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS issue_books (
    issue_id INT PRIMARY KEY AUTO_INCREMENT,
    book_id INT,
    student_id INT,
    issue_date DATE,
    due_date DATE,
    return_date DATE,
    status VARCHAR(20),
    FOREIGN KEY (book_id) REFERENCES books(book_id),
    FOREIGN KEY (student_id) REFERENCES students(student_id)
);

CREATE TABLE IF NOT EXISTS fine (
    fine_id INT AUTO_INCREMENT PRIMARY KEY,
    issue_id INT NOT NULL,
    student_id INT NOT NULL,
    days_late INT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    paid ENUM('YES','NO') DEFAULT 'NO',
    FOREIGN KEY (issue_id) REFERENCES issue_books(issue_id),
    FOREIGN KEY (student_id) REFERENCES students(student_id)
);

CREATE INDEX idx_books_title ON books(title);
CREATE INDEX idx_issue_status ON issue_books(status);
CREATE INDEX idx_fine_paid ON fine(paid);