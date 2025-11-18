import java.time.LocalDate;

/**
 * Model class jo 'Employee' table ke data ko hold karti hai.
 */
public class Employee {

    // Database 'Employee' table ke columns
    private int empId;
    private String name;
    private String department;
    private String designation;
    private LocalDate joiningDate;
    private String contactNumber;

    // Default constructor (GUI libraries ko iski zaroorat padti hai)
    public Employee() {
    }

    // Constructor (Humne addEmployee mein use kiya hai)
    public Employee(int empId, String name, String department, String designation, LocalDate joiningDate, String contactNumber) {
        this.empId = empId;
        this.name = name;
        this.department = department;
        this.designation = designation;
        this.joiningDate = joiningDate;
        this.contactNumber = contactNumber;
    }


    // --- Getters and Setters ---
    // (Yahi methods missing the, jiske errors aa rahe the)

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    // toString() method (console par print karne ke liye, optional)
    @Override
    public String toString() {
        return "Employee [empId=" + empId + ", name=" + name + ", designation=" + designation + "]";
    }
}