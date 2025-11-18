# Employee Payroll Management System (Java Swing GUI)

![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql)
![Java Swing](https://img.shields.io/badge/Java%20Swing-GUI-blue?style=for-the-badge&logo=java)

A comprehensive, secure, and multi-user desktop application for managing employee payroll, built with Java Swing and MySQL. This project evolved from a simple console application into a full-fledged, real-world GUI application featuring role-based access and secure password hashing.

---

## 🚀 Core Features

This application is built with a clean, multi-layered architecture and includes several professional-grade features:

* **Secure Hashed Login:** User passwords are **never** stored in plain text. All passwords are hashed using the **SHA-256 algorithm** via the `PasswordUtil` class before being stored and compared in the database.
* **Role-Based Access Control (RBAC):** The system supports two distinct user roles with different GUIs and permissions:
    * **ADMIN Role:** Has full access to the system. Can perform all CRUD operations (Create, Read, Update, Delete) on employees and generate payroll for anyone.
    * **EMPLOYEE Role:** Has a limited, "self-service" dashboard. Can only view their own personal details and check their complete payslip history.
* **Full Admin Dashboard (CRUD):** A tabbed dashboard (`JTabbedPane`) for admins to manage the entire employee lifecycle:
    * **Create:** Add new employees via a validated form.
    * **Read:** View all employees in a sortable `JTable`.
    * **Update:** Select an employee from the table to auto-fill the form for updates.
    * **Delete:** Securely delete employee records with a confirmation dialog.
* **Employee Self-Service Portal:** A separate, read-only dashboard for employees to view their own profile and payslip history, retrieved dynamically from the database.
* **Persistent Payroll History:** When an Admin generates a payslip, the record is automatically saved to the `Payroll` table in the database for future reference.
* **Clean Architecture (DAO Pattern):** The code is strictly decoupled. The GUI (View) is completely separate from the database logic (Data Access Layer), making the project easy to maintain and scale.

---

## 🛠️ Technology Stack

* **Frontend (GUI):** `Java Swing` (using `JFrame`, `JPanel`, `JTable`, `JTabbedPane`, `JOptionPane`).
* **Backend (Logic):** `Java (JDK 17+)`
* **Database:** `MySQL 8.0`
* **Connectivity:** `JDBC` (MySQL Connector/J)
* **Security:** `java.security.MessageDigest` (for SHA-256 Hashing)
* **IDE:** IntelliJ IDEA / VS Code

---

## 🏛️ Project Architecture

The application follows a 3-Tier Architecture, implemented using the **Data Access Object (DAO) Pattern** to ensure a clean separation of concerns.



[Image of a 3-Tier Architecture diagram]


1.  **Presentation Layer (View):**
    * `LoginScreen.java`: Handles user login.
    * `MainApplication.java`: The main dashboard window that contains all other GUI components and tabs.
2.  **Business Logic Layer (BLL):**
    * `PayrollCalculator.java`: Contains the formulas and logic for calculating gross/net salary.
    * `PasswordUtil.java`: A helper class dedicated to hashing passwords with SHA-256.
3.  **Data Access Layer (DAL):**
    * `DBConnection.java`: Manages the database connection.
    * `UserDAO.java`: Handles all database operations for the `Users` table (e.g., `validateUser`).
    * `EmployeeDAO.java`: Handles all CRUD operations for the `Employee` table.
    * `SalaryStructureDAO.java`: Fetches salary rules from the database.
    * `PayrollDAO.java`: Handles saving and fetching payslip history from the `Payroll` table.
4.  **Model Layer (POJO):**
    * `Employee.java`, `Payroll.java`, `SalaryStructure.java`, `LoggedInUser.java`: Simple Java classes that act as data containers (data models).



---

## 📸 Screenshots

*(Yahan aap apne project ke screenshots add kar sakte hain. GitHub par image add karne ke liye, aap image ko repository mein drag-and-drop kar sakte hain aur phir uske link ko yahan daal sakte hain)*

**1. Secure Login Screen**
`[Yahan Login Screen ka screenshot paste karein]`

**2. Admin Dashboard (Employee Management Tab)**
`[Yahan Admin Dashboard ka screenshot paste karein]`

**3. Admin - Generate Payslip Tab**
`[Yahan Payslip Tab ka screenshot paste karein]`

**4. Employee Dashboard (Self-Service Portal)**
`[Yahan Employee Dashboard ka screenshot paste karein]`

**5. Database (Hashed Passwords)**
`[Yahan MySQL Users Table ka screenshot paste karein]`

---

## ⚙️ How to Run Locally

1.  **Clone the Repository:**
    ```bash
    git clone [https://github.com/](https://github.com/)[Your-Username]/[Your-Repo-Name].git
    ```
2.  **Database Setup:**
    * Open MySQL Workbench and create a new database schema named `epms_db`.
    * Run the SQL scripts located in the `/database_setup.sql` file (ya yahan queries paste kar dein) to create the `Users`, `Employee`, `Salary_Structure`, and `Payroll` tables.
    * **Crucially, you must create the Admin/Employee users** by first hashing their passwords (using `PasswordUtil.java`) and then inserting them into the `Users` table.
3.  **Configure Connection:**
    * Open `src/DBConnection.java`.
    * Update the `PASSWORD` variable with your MySQL root password.
4.  **Run:**
    * Open the project in your favorite IDE (IntelliJ/Eclipse/VS Code).
    * Add the `mysql-connector-j-X.X.XX.jar` file to your project's build path/libraries.
    * Run the `main` method in `LoginScreen.java`.

**Default Logins:**
* **Admin:** `username: admin` / `password: pass123`
* **Employee:** `username: priya` / `password: priya123`