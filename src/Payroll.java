/**
 * Model class jo 'Payroll' data (History aur Calculation) ko hold karti hai.
 */
public class Payroll {

    // --- Database 'Payroll' table ke Columns ---
    private int payrollId;
    private int empId;
    private int month;
    private int year;
    private double grossSalary;
    private double netSalary;

    // --- Calculation/Display ke liye Extra Columns ---
    private String empName;
    private String designation;
    private double pfDeduction;
    private double taxDeduction;
    private double totalDeductions;


    // --- Getters and Setters ---
    // (Yahi methods missing the 'PayrollDAO' aur 'MainApplication' mein)

    public int getPayrollId() {
        return payrollId;
    }

    public void setPayrollId(int payrollId) {
        this.payrollId = payrollId;
    }

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getGrossSalary() {
        return grossSalary;
    }

    public void setGrossSalary(double grossSalary) {
        this.grossSalary = grossSalary;
    }

    public double getNetSalary() {
        return netSalary;
    }

    public void setNetSalary(double netSalary) {
        this.netSalary = netSalary;
    }

    // --- Calculation/Display waale fields ke Getters/Setters ---

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public double getPfDeduction() {
        return pfDeduction;
    }

    public void setPfDeduction(double pfDeduction) {
        this.pfDeduction = pfDeduction;
    }

    public double getTaxDeduction() {
        return taxDeduction;
    }

    public void setTaxDeduction(double taxDeduction) {
        this.taxDeduction = taxDeduction;
    }

    public double getTotalDeductions() {
        return totalDeductions;
    }

    public void setTotalDeductions(double totalDeductions) {
        this.totalDeductions = totalDeductions;
    }
}