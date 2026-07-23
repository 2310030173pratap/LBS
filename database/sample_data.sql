USE library;

INSERT INTO books (book_id, title, author, category, price, quantity) VALUES
(1, 'Java Programming', 'James', 'Programming', 550.00, 7),
(2, 'Operating System', 'Galvin', 'Computer Science', 650.00, 8),
(3, 'Database System', 'Korth', 'Database', 700.00, 5),
(4, 'Data Structures', 'Seymour', 'Programming', 500.00, 10),
(5, 'Software Engineering', 'Pressman', 'Software', 800.00, 3);

INSERT INTO students (student_id, student_name, department, year, phone, password) VALUES
(101, 'Pratap', 'CSE', 3, '9876543210', 'student123'),
(102, 'Anita', 'IT', 2, '9876543211', 'student123'),
(103, 'Rahul', 'ECE', 4, '9876543212', 'student123'),
(104, 'Meena', 'CSE', 1, '9876543213', 'student123'),
(105, 'Vikram', 'EEE', 3, '9876543214', 'student123');

INSERT INTO admin (admin_id, username, password) VALUES
(1, 'admin', 'admin123');

INSERT INTO issue_books (book_id, student_id, issue_date, due_date, return_date, status) VALUES
(1, 101, CURRENT_DATE(), DATE_ADD(CURRENT_DATE(), INTERVAL 7 DAY), NULL, 'Issued');

INSERT INTO fine (issue_id, student_id, days_late, amount, paid) VALUES
(1, 101, 0, 0.00, 'NO');