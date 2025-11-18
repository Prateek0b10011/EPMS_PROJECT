import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;
import java.time.LocalDate; // Month/Year nikaalne ke liye

/**
 * PayrollDAO (Data Access Object)
 * Yeh class generate kiye gaye payroll data ko database mein save karne ke liye responsible hai.
 */
public class PayrollDAO {

    // YEH HAMARI ORIGINAL 'Payroll' TABLE SE MATCH KARTA HAI
    private static final String INSERT_PAYROLL_SQL =
            "INSERT INTO Payroll (emp_id, month, year, gross_salary, net_salary) VALUES (?, ?, ?, ?, ?)";

    /**
     * Ek naye payroll record ko database mein add karta hai.
     *
     * @param payroll - Payroll object jismein saari calculated details hain.
     * @return true agar record save ho gaya, false agar koi error hua.
     */
    public boolean addPayroll(Payroll payroll) {

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(INSERT_PAYROLL_SQL)) {

            // Set values for the placeholders (?) in the query.
            pstmt.setInt(1, payroll.getEmpId());

            // Humne table mein month aur year columns banaye the
            pstmt.setInt(2, LocalDate.now().getMonthValue()); // Current Month (e.g., 11)
            pstmt.setInt(3, LocalDate.now().getYear());     // Current Year (e.g., 2025)

            pstmt.setDouble(4, payroll.getGrossSalary());
            pstmt.setDouble(5, payroll.getNetSalary());

            // executeUpdate() returns the number of rows affected.
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error saving payroll data: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    // (Is method ko PayrollDAO class ke andar paste karein)

    /**
     * Ek specific employee ID ke saare puraane payroll records fetch karta hai.
     */
    public java.util.List<Payroll> getPayslipsByEmployeeId(int empId) {
        java.util.List<Payroll> payslips = new java.util.ArrayList<>();

        // Query: Sirf uss employee ke records laao
        String query = "SELECT * FROM Payroll WHERE emp_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setInt(1, empId);
            java.sql.ResultSet rs = pstmt.executeQuery();

            // Loop jab tak saare records milte hain
            while (rs.next()) {
                Payroll payroll = new Payroll();

                // Database columns se values set karo
                payroll.setPayrollId(rs.getInt("payroll_id"));
                payroll.setEmpId(rs.getInt("emp_id"));
                payroll.setMonth(rs.getInt("month"));
                payroll.setYear(rs.getInt("year"));
                payroll.setGrossSalary(rs.getDouble("gross_salary"));
                payroll.setNetSalary(rs.getDouble("net_salary"));

                payslips.add(payroll); // List mein add karo
            }

        } catch (SQLException e) {
            System.err.println("Error fetching payslip history: " + e.getMessage());
            e.printStackTrace();
        }

        return payslips; // Poori list return karo
    }
}
