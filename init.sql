-- Create Database if not exists (redundant but safe)
CREATE DATABASE IF NOT EXISTS epms_db;
USE epms_db;

-- 1. Create Employee Table
CREATE TABLE IF NOT EXISTS Employee (
    emp_id INT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    department VARCHAR(100) NOT NULL,
    designation VARCHAR(100) NOT NULL,
    joining_date DATE NOT NULL,
    contact_number VARCHAR(20) NOT NULL
);

-- 2. Create Users Table
CREATE TABLE IF NOT EXISTS Users (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(256) NOT NULL,
    role VARCHAR(20) NOT NULL,
    linked_emp_id INT,
    FOREIGN KEY (linked_emp_id) REFERENCES Employee(emp_id) ON DELETE SET NULL
);

-- 3. Create Salary_Structure Table
CREATE TABLE IF NOT EXISTS Salary_Structure (
    designation VARCHAR(100) PRIMARY KEY,
    basic_salary DOUBLE NOT NULL,
    hra DOUBLE NOT NULL,
    da DOUBLE NOT NULL,
    pf_percent DOUBLE NOT NULL,
    tax_percent DOUBLE NOT NULL
);

-- 4. Create Payroll Table
CREATE TABLE IF NOT EXISTS Payroll (
    payroll_id INT AUTO_INCREMENT PRIMARY KEY,
    emp_id INT NOT NULL,
    month INT NOT NULL,
    year INT NOT NULL,
    gross_salary DOUBLE NOT NULL,
    net_salary DOUBLE NOT NULL,
    FOREIGN KEY (emp_id) REFERENCES Employee(emp_id) ON DELETE CASCADE
);

-- Seed Initial Salary Structures
INSERT INTO Salary_Structure (designation, basic_salary, hra, da, pf_percent, tax_percent)
VALUES 
('Manager', 80000.0, 15000.0, 10000.0, 12.0, 10.0),
('Recruiter', 40000.0, 8000.0, 5000.0, 12.0, 5.0),
('Senior Recruiter', 55000.0, 10000.0, 6000.0, 12.0, 7.0),
('Software Engineer', 60000.0, 12000.0, 8000.0, 12.0, 8.0)
ON DUPLICATE KEY UPDATE 
basic_salary = VALUES(basic_salary),
hra = VALUES(hra),
da = VALUES(da),
pf_percent = VALUES(pf_percent),
tax_percent = VALUES(tax_percent);

-- Seed Initial Employees
INSERT INTO Employee (emp_id, name, department, designation, joining_date, contact_number)
VALUES
(1, 'Admin User', 'IT', 'Manager', '2026-01-01', '1234567890'),
(2, 'Priya Sharma', 'HR', 'Recruiter', '2026-02-15', '9876543210')
ON DUPLICATE KEY UPDATE
name = VALUES(name),
department = VALUES(department),
designation = VALUES(designation),
joining_date = VALUES(joining_date),
contact_number = VALUES(contact_number);

-- Seed Initial Users (Passwords hashed using SHA-256 via PasswordUtil)
-- admin / pass123
-- priya / priya123
INSERT INTO Users (username, password, role, linked_emp_id)
VALUES
('admin', '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c', 'ADMIN', 1),
('priya', '0a8b8dfad3f637d7d30fed7b108c5c5986c4775d14cab26ec9279866eba99116', 'EMPLOYEE', 2)
ON DUPLICATE KEY UPDATE
password = VALUES(password),
role = VALUES(role),
linked_emp_id = VALUES(linked_emp_id);
