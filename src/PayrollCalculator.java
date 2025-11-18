public class PayrollCalculator {

    /**
     * Yeh method ek employee aur uske salary structure ke basis par ek poora Payroll object generate karta hai.
     * YEH HAI MAIN CALCULATION LOGIC (VIVA ke liye important).
     *
     * @param employee  Jiska payroll calculate karna hai.
     * @param structure Uske designation ka salary structure.
     * @return Ek Payroll object jismein saari calculated details hain.
     */
    public Payroll generatePayroll(Employee employee, SalaryStructure structure) {
        if (employee == null || structure == null) {
            System.err.println("Cannot generate payroll. Employee or Salary Structure is missing.");
            return null;
        }

        // 1. Gross Salary calculate karo
        // Gross Salary = Basic + HRA + DA
        double grossSalary = structure.getBasicSalary() + structure.getHra() + structure.getDa();

        // 2. Deductions calculate karo
        double pfDeduction = (structure.getBasicSalary() * structure.getPfPercent()) / 100.0;
        double taxDeduction = (grossSalary * structure.getTaxPercent()) / 100.0;
        double totalDeductions = pfDeduction + taxDeduction;

        // 3. Net Salary calculate karo
        // Net Salary = Gross Salary - Total Deductions
        double netSalary = grossSalary - totalDeductions;

        // 4. Saari details ek Payroll object mein daal kar return karo
        Payroll payroll = new Payroll();
        payroll.setEmpId(employee.getEmpId());
        payroll.setEmpName(employee.getName());
        payroll.setDesignation(employee.getDesignation());
        payroll.setGrossSalary(grossSalary);
        payroll.setPfDeduction(pfDeduction);
        payroll.setTaxDeduction(taxDeduction);
        payroll.setTotalDeductions(totalDeductions);
        payroll.setNetSalary(netSalary);

        return payroll;
    }

    // --- Is class ko test karne ke liye main method ---
    public static void main(String[] args) {
        // 1. DAOs initialize karo
        EmployeeDAO employeeDAO = new EmployeeDAO();
        SalaryStructureDAO salaryDAO = new SalaryStructureDAO();

        // 2. Employee data fetch karo
        Employee employee = employeeDAO.getEmployeeById(101); // Maan lete hain 101 Amit Sharma (Java Developer) hai

        if (employee == null) {
            System.out.println("Employee 101 not found for salary calculation.");
            return;
        }

        // 3. Employee ke designation ke basis par salary rules fetch karo
        SalaryStructure structure = salaryDAO.getSalaryStructureByDesignation(employee.getDesignation());

        if (structure == null) {
            System.out.println("Salary structure not found for designation: " + employee.getDesignation());
            return;
        }

        // 4. Calculator se payroll generate karo
        PayrollCalculator calculator = new PayrollCalculator();
        Payroll payroll = calculator.generatePayroll(employee, structure);

        if (payroll != null) {
            System.out.println("--- Generated Payslip for " + payroll.getEmpName() + " ---");
            System.out.println("Employee ID: " + payroll.getEmpId());
            System.out.println("Designation: " + payroll.getDesignation());
            System.out.println("------------------------------------");
            System.out.printf("Gross Salary: %.2f%n", payroll.getGrossSalary());
            System.out.printf("PF Deduction: %.2f%n", payroll.getPfDeduction());
            System.out.printf("Tax Deduction: %.2f%n", payroll.getTaxDeduction());
            System.out.printf("Total Deductions: %.2f%n", payroll.getTotalDeductions());
            System.out.println("------------------------------------");
            System.out.printf("Net Salary: %.2f%n", payroll.getNetSalary());
            System.out.println("------------------------------------");
        } else {
            System.out.println("Failed to generate payroll for " + employee.getName());
        }
    }
}
