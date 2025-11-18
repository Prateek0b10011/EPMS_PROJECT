import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Yeh DAO class Salary_Structure table se data fetch karne ke liye responsible hai.
 */
public class SalaryStructureDAO {

    private static final String SELECT_SALARY_STRUCTURE_BY_DESIGNATION = "SELECT basic_salary, hra, da, pf_percent, tax_percent FROM Salary_Structure WHERE designation = ?";

    /**
     * Designation ke basis par salary rules laata hai.
     * @param designation - Employee ka designation.
     * @return SalaryStructure object agar mil gaya, varna null.
     */
    public SalaryStructure getSalaryStructureByDesignation(String designation) {
        SalaryStructure structure = null;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(SELECT_SALARY_STRUCTURE_BY_DESIGNATION)) {

            pstmt.setString(1, designation);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    structure = new SalaryStructure();
                    structure.setDesignation(designation);
                    structure.setBasicSalary(rs.getDouble("basic_salary"));
                    structure.setHra(rs.getDouble("hra"));
                    structure.setDa(rs.getDouble("da"));
                    structure.setPfPercent(rs.getDouble("pf_percent"));
                    structure.setTaxPercent(rs.getDouble("tax_percent"));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error fetching salary structure for designation: " + designation);
            e.printStackTrace();
        }

        return structure;
    }
}