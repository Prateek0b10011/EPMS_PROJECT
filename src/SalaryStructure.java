/**
 * Yeh class ek "Data Transfer Object" (DTO) hai.
 * Iska kaam sirf Salary Structure table se data ko Java mein hold karna hai.
 * Ismein koi complex logic nahi hota.
 */
public class SalaryStructure {

    private String designation;
    private double basicSalary;
    private double hra; // House Rent Allowance
    private double da;  // Dearness Allowance
    private double pfPercent; // Provident Fund percentage
    private double taxPercent;

    // Constructors
    public SalaryStructure() {
    }

    public SalaryStructure(String designation, double basicSalary, double hra, double da, double pfPercent, double taxPercent) {
        this.designation = designation;
        this.basicSalary = basicSalary;
        this.hra = hra;
        this.da = da;
        this.pfPercent = pfPercent;
        this.taxPercent = taxPercent;
    }

    // --- Getters and Setters ---

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public double getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(double basicSalary) {
        this.basicSalary = basicSalary;
    }

    public double getHra() {
        return hra;
    }

    public void setHra(double hra) {
        this.hra = hra;
    }

    public double getDa() {
        return da;
    }

    public void setDa(double da) {
        this.da = da;
    }

    public double getPfPercent() {
        return pfPercent;
    }

    public void setPfPercent(double pfPercent) {
        this.pfPercent = pfPercent;
    }

    public double getTaxPercent() {
        return taxPercent;
    }

    public void setTaxPercent(double taxPercent) {
        this.taxPercent = taxPercent;
    }
}