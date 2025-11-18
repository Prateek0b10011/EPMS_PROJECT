import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * EmployeeDAO (Data Access Object)
 * Yeh class database se related saare operations handle karti hai.
 * Iska kaam sirf database se data laana aur bhejna hai.
 * Isse code clean aur organized rehta hai.
 */
public class EmployeeDAO {

    // --- SQL Queries ---
    // Saare SQL commands yahan ek saath rakhna ek acchi practice hai.
    // Isse queries manage karna aasan ho jaata hai.
    private static final String INSERT_EMPLOYEE_SQL = "INSERT INTO Employee (emp_id, name, department, designation, joining_date, contact_number) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_EMPLOYEE_BY_ID_SQL = "SELECT * FROM Employee WHERE emp_id = ?";
    private static final String SELECT_ALL_EMPLOYEES_SQL = "SELECT * FROM Employee";
    private static final String UPDATE_EMPLOYEE_SQL = "UPDATE Employee SET name = ?, department = ?, designation = ?, joining_date = ?, contact_number = ? WHERE emp_id = ?";
    private static final String DELETE_EMPLOYEE_SQL = "DELETE FROM Employee WHERE emp_id = ?";

    /**
     * Naye employee ko database mein add karta hai.
     * @param employee - Employee object jismein saari details hain.
     * @return true agar employee add ho gaya, false agar nahi hua.
     */
    public boolean addEmployee(Employee employee) {
        // try-with-resources: Connection aur PreparedStatement automatically close ho jaate hain.
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(INSERT_EMPLOYEE_SQL)) {

            // Placeholders (?) mein values set karna.
            pstmt.setInt(1, employee.getEmpId());
            pstmt.setString(2, employee.getName());
            pstmt.setString(3, employee.getDepartment());
            pstmt.setString(4, employee.getDesignation());
            pstmt.setDate(5, Date.valueOf(employee.getJoiningDate())); // LocalDate ko SQL Date mein convert karna.
            pstmt.setString(6, employee.getContactNumber());

            // executeUpdate() rows affected ka count return karta hai.
            // Agar 1 row affect hui, matlab data insert ho gaya.
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error adding employee: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Employee ID ke basis par ek employee ki details laata hai.
     * @param empId - Employee ki ID.
     * @return Employee object agar mil gaya, varna null.
     */
    public Employee getEmployeeById(int empId) {
        Employee employee = null;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(SELECT_EMPLOYEE_BY_ID_SQL)) {

            pstmt.setInt(1, empId);

            // ResultSet ko bhi try-with-resources mein daalna best practice hai.
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // ResultSet se data nikaal kar Employee object mein daalna.
                    employee = mapResultSetToEmployee(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching employee by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return employee;
    }

    /**
     * Database se saare employees ki list laata hai.
     * @return List of Employee objects.
     */
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_EMPLOYEES_SQL)) {

            // Jab tak rows milti rahein, loop chalao.
            while (rs.next()) {
                Employee employee = mapResultSetToEmployee(rs);
                employees.add(employee);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all employees: " + e.getMessage());
            e.printStackTrace();
        }
        return employees;
    }

    /**
     * Maujooda employee ki details update karta hai.
     * @param employee - Employee object jismein updated details hain.
     * @return true agar update successful hua, varna false.
     */
    public boolean updateEmployee(Employee employee) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(UPDATE_EMPLOYEE_SQL)) {

            pstmt.setString(1, employee.getName());
            pstmt.setString(2, employee.getDepartment());
            pstmt.setString(3, employee.getDesignation());
            pstmt.setDate(4, Date.valueOf(employee.getJoiningDate()));
            pstmt.setString(5, employee.getContactNumber());
            pstmt.setInt(6, employee.getEmpId()); // WHERE clause ke liye ID.

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error updating employee: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Employee ko uski ID se delete karta hai.
     * @param empId - Jiss employee ko delete karna hai uski ID.
     * @return true agar deletion successful hua, varna false.
     */
    public boolean deleteEmployee(int empId) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(DELETE_EMPLOYEE_SQL)) {

            pstmt.setInt(1, empId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting employee: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Ek helper method jo ResultSet se data ko Employee object mein convert karta hai.
     * Isse code repeat nahi hota (DRY Principle: Don't Repeat Yourself).
     */
    private Employee mapResultSetToEmployee(ResultSet rs) throws SQLException {
        Employee employee = new Employee();
        employee.setEmpId(rs.getInt("emp_id"));
        employee.setName(rs.getString("name"));
        employee.setDepartment(rs.getString("department"));
        employee.setDesignation(rs.getString("designation"));

        // Date null ho sakti hai, isliye check karna zaroori hai.
        if (rs.getDate("joining_date") != null) {
            employee.setJoiningDate(rs.getDate("joining_date").toLocalDate());
        }
        employee.setContactNumber(rs.getString("contact_number"));
        return employee;
    }

    // --- Is class ko test karne ke liye main method ---
    public static void main(String[] args) {
        EmployeeDAO dao = new EmployeeDAO();

        // 1. Naya Employee Add karna
        System.out.println("--- Testing Add Employee ---");
        Employee newEmp = new Employee();
        newEmp.setEmpId(103);
        newEmp.setName("Riya Singh");
        newEmp.setDepartment("HR");
        newEmp.setDesignation("Recruiter");
        newEmp.setJoiningDate(LocalDate.of(2024, 5, 20));
        newEmp.setContactNumber("9876543212");

        if (dao.addEmployee(newEmp)) {
            System.out.println("Riya Singh added successfully!");
        } else {
            System.out.println("Failed to add Riya Singh.");
        }

        // 2. Ek Employee ko ID se dhoondhna
        System.out.println("\n--- Testing Get Employee By ID ---");
        Employee foundEmp = dao.getEmployeeById(101); // Maan lete hain 101 pehle se database mein hai
        if (foundEmp != null) {
            System.out.println("Found Employee 101: " + foundEmp);
        } else {
            System.out.println("Employee 101 not found.");
        }

        // 3. Saare Employees ki list dekhna
        System.out.println("\n--- Testing Get All Employees ---");
        List<Employee> allEmployees = dao.getAllEmployees();
        System.out.println("Total employees found: " + allEmployees.size());
        // Lambda expression se har employee ko print karna
        allEmployees.forEach(emp -> System.out.println(emp));

        // 4. Employee ki details Update karna
        System.out.println("\n--- Testing Update Employee ---");
        Employee empToUpdate = dao.getEmployeeById(103);
        if (empToUpdate != null) {
            empToUpdate.setDesignation("Senior Recruiter"); // Designation update kar rahe hain
            if (dao.updateEmployee(empToUpdate)) {
                System.out.println("Employee 103 updated successfully. New details: " + dao.getEmployeeById(103));
            } else {
                System.out.println("Failed to update employee 103.");
            }
        }

        // 5. Employee ko Delete karna
        System.out.println("\n--- Testing Delete Employee ---");
        if (dao.deleteEmployee(103)) {
            System.out.println("Employee 103 deleted successfully.");
        } else {
            System.out.println("Failed to delete employee 103.");
        }
    }
}