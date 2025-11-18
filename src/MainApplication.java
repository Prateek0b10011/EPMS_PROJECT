import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class MainApplication extends JFrame {

    // Backend Connections
    private final EmployeeDAO employeeDAO = new EmployeeDAO();
    private final SalaryStructureDAO salaryDAO = new SalaryStructureDAO();
    private final PayrollDAO payrollDAO = new PayrollDAO();
    private final PayrollCalculator calculator = new PayrollCalculator();
    private final LoggedInUser user;

    // UI Components
    private JTabbedPane tabbedPane;
    private Font mainFont = new Font("Segoe UI", Font.PLAIN, 14);

    // Admin Tab Components
    private JTable employeeTable;
    private DefaultTableModel employeeTableModel;
    private JTextField empIdText, empNameText, empDeptText, empDesignationText, empContactText, empJoiningDateText;
    private JTextField payslipEmpIdText;
    private JTextArea payslipArea;

    // Employee Tab Components
    private JTextArea myDetailsArea;
    private JTable myPayslipTable;
    private DefaultTableModel myPayslipTableModel;

    public MainApplication(LoggedInUser user) {
        this.user = user;

        // 1. Window Setup
        setTitle("EPMS Dashboard - Welcome " + user.getUsername());
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main Layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));
        setContentPane(mainPanel);

        // Header
        JLabel headerLabel = new JLabel("Employee Payroll Management System", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setOpaque(true);
        headerLabel.setBackground(new Color(0, 80, 157)); // Blue
        headerLabel.setPreferredSize(new Dimension(1000, 60));
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        // Tabs
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(mainFont);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // Role Check
        if (user.isAdmin()) {
            tabbedPane.addTab("Manage Employees", createAdminEmployeePanel());
            tabbedPane.addTab("Generate Payslip", createAdminPayslipPanel());
        } else {
            tabbedPane.addTab("My Profile", createEmployeeProfilePanel());
        }

        // Logout Button
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(220, 53, 69));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFont(mainFont);

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.add(logoutButton);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        logoutButton.addActionListener(e -> {
            dispose();
            new LoginScreen();
        });

        // --- IMPORTANT: Window ko dikhana ---
        setVisible(true);
    }

    // ---------------------------------------------------------
    // ADMIN PANELS
    // ---------------------------------------------------------

    private JPanel createAdminEmployeePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Form (Left)
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Employee Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fields setup
        empIdText = new JTextField(15);
        empNameText = new JTextField(15);
        empDeptText = new JTextField(15);
        empDesignationText = new JTextField(15);
        empContactText = new JTextField(15);
        empJoiningDateText = new JTextField(15);

        addFormField(formPanel, gbc, 0, "ID:", empIdText);
        addFormField(formPanel, gbc, 1, "Name:", empNameText);
        addFormField(formPanel, gbc, 2, "Dept:", empDeptText);
        addFormField(formPanel, gbc, 3, "Desig:", empDesignationText);
        addFormField(formPanel, gbc, 4, "Contact:", empContactText);
        addFormField(formPanel, gbc, 5, "Join Date:", empJoiningDateText);

        // Buttons
        JPanel btnPanel = new JPanel(new GridLayout(1, 4, 5, 5));
        JButton btnAdd = new JButton("Add");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnClear = new JButton("Clear");

        btnAdd.setBackground(new Color(40, 167, 69)); btnAdd.setForeground(Color.WHITE);
        btnUpdate.setBackground(new Color(0, 123, 255)); btnUpdate.setForeground(Color.WHITE);
        btnDelete.setBackground(new Color(220, 53, 69)); btnDelete.setForeground(Color.WHITE);

        btnPanel.add(btnAdd); btnPanel.add(btnUpdate); btnPanel.add(btnDelete); btnPanel.add(btnClear);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        formPanel.add(btnPanel, gbc);

        panel.add(formPanel, BorderLayout.WEST);

        // Table (Center)
        String[] cols = {"ID", "Name", "Dept", "Designation", "Contact", "Date"};
        employeeTableModel = new DefaultTableModel(cols, 0);
        employeeTable = new JTable(employeeTableModel);
        employeeTable.setRowHeight(25);

        employeeTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = employeeTable.getSelectedRow();
                if(row != -1) {
                    empIdText.setText(employeeTableModel.getValueAt(row, 0).toString());
                    empNameText.setText(employeeTableModel.getValueAt(row, 1).toString());
                    empDeptText.setText(employeeTableModel.getValueAt(row, 2).toString());
                    empDesignationText.setText(employeeTableModel.getValueAt(row, 3).toString());
                    empContactText.setText(employeeTableModel.getValueAt(row, 4).toString());
                    empJoiningDateText.setText(employeeTableModel.getValueAt(row, 5).toString());
                }
            }
        });

        panel.add(new JScrollPane(employeeTable), BorderLayout.CENTER);

        // Listeners
        btnAdd.addActionListener(e -> addEmployee());
        btnUpdate.addActionListener(e -> updateEmployee());
        btnDelete.addActionListener(e -> deleteEmployee());
        btnClear.addActionListener(e -> clearForm());

        loadEmployeeData();
        return panel;
    }

    private JPanel createAdminPayslipPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10,10,10,10));

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Employee ID:"));
        payslipEmpIdText = new JTextField(10);
        topPanel.add(payslipEmpIdText);
        JButton btnGen = new JButton("Generate & Save Payslip");
        btnGen.setBackground(new Color(0, 123, 255));
        btnGen.setForeground(Color.WHITE);
        topPanel.add(btnGen);
        panel.add(topPanel, BorderLayout.NORTH);

        payslipArea = new JTextArea();
        payslipArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        panel.add(new JScrollPane(payslipArea), BorderLayout.CENTER);

        btnGen.addActionListener(e -> generatePayslip());
        return panel;
    }

    // ---------------------------------------------------------
    // EMPLOYEE PANEL
    // ---------------------------------------------------------

    private JPanel createEmployeeProfilePanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        panel.setBorder(new EmptyBorder(10,10,10,10));

        myDetailsArea = new JTextArea();
        myDetailsArea.setFont(new Font("Monospaced", Font.BOLD, 15));
        myDetailsArea.setEditable(false);
        myDetailsArea.setBorder(BorderFactory.createTitledBorder("My Details"));
        panel.add(new JScrollPane(myDetailsArea));

        String[] cols = {"ID", "Month", "Year", "Gross", "Net Salary"};
        myPayslipTableModel = new DefaultTableModel(cols, 0);
        myPayslipTable = new JTable(myPayslipTableModel);
        JScrollPane scroll = new JScrollPane(myPayslipTable);
        scroll.setBorder(BorderFactory.createTitledBorder("My Payslip History"));
        panel.add(scroll);

        loadMyData();
        return panel;
    }

    // ---------------------------------------------------------
    // HELPERS & LOGIC
    // ---------------------------------------------------------

    private void addFormField(JPanel p, GridBagConstraints gbc, int y, String lbl, JTextField txt) {
        gbc.gridx = 0; gbc.gridy = y; p.add(new JLabel(lbl), gbc);
        gbc.gridx = 1; p.add(txt, gbc);
    }

    private void loadEmployeeData() {
        employeeTableModel.setRowCount(0);
        List<Employee> list = employeeDAO.getAllEmployees();
        for(Employee e : list) {
            employeeTableModel.addRow(new Object[]{e.getEmpId(), e.getName(), e.getDepartment(), e.getDesignation(), e.getContactNumber(), e.getJoiningDate()});
        }
    }

    private void clearForm() {
        empIdText.setText(""); empNameText.setText(""); empDeptText.setText("");
        empDesignationText.setText(""); empContactText.setText(""); empJoiningDateText.setText("");
    }

    private void addEmployee() {
        try {
            Employee e = new Employee(Integer.parseInt(empIdText.getText()), empNameText.getText(), empDeptText.getText(), empDesignationText.getText(), LocalDate.parse(empJoiningDateText.getText()), empContactText.getText());
            if(employeeDAO.addEmployee(e)) { JOptionPane.showMessageDialog(this, "Added!"); loadEmployeeData(); clearForm(); }
        } catch(Exception ex) { JOptionPane.showMessageDialog(this, "Error: Check Data"); }
    }

    private void updateEmployee() {
        try {
            Employee e = new Employee(Integer.parseInt(empIdText.getText()), empNameText.getText(), empDeptText.getText(), empDesignationText.getText(), LocalDate.parse(empJoiningDateText.getText()), empContactText.getText());
            if(employeeDAO.updateEmployee(e)) { JOptionPane.showMessageDialog(this, "Updated!"); loadEmployeeData(); }
        } catch(Exception ex) { JOptionPane.showMessageDialog(this, "Error: Check Data"); }
    }

    private void deleteEmployee() {
        try {
            int id = Integer.parseInt(empIdText.getText());
            if(employeeDAO.deleteEmployee(id)) { JOptionPane.showMessageDialog(this, "Deleted!"); loadEmployeeData(); clearForm(); }
        } catch(Exception ex) { JOptionPane.showMessageDialog(this, "Select Employee first"); }
    }

    private void generatePayslip() {
        try {
            int id = Integer.parseInt(payslipEmpIdText.getText());
            Employee e = employeeDAO.getEmployeeById(id);
            if(e == null) { payslipArea.setText("Not Found"); return; }

            SalaryStructure s = salaryDAO.getSalaryStructureByDesignation(e.getDesignation());
            if(s == null) { payslipArea.setText("Salary Structure Missing"); return; }

            Payroll p = calculator.generatePayroll(e, s);
            payrollDAO.addPayroll(p); // Save to DB

            payslipArea.setText("PAYSLIP GENERATED & SAVED\n-------------------------\nName: " + e.getName() + "\nNet Salary: " + p.getNetSalary());
        } catch(Exception ex) { payslipArea.setText("Invalid ID"); }
    }

    private void loadMyData() {
        Employee e = employeeDAO.getEmployeeById(user.getLinkedEmpId());
        if(e != null) myDetailsArea.setText("Name: " + e.getName() + "\nDept: " + e.getDepartment() + "\nRole: " + e.getDesignation());

        List<Payroll> list = payrollDAO.getPayslipsByEmployeeId(user.getLinkedEmpId());
        for(Payroll p : list) {
            myPayslipTableModel.addRow(new Object[]{p.getPayrollId(), p.getMonth(), p.getYear(), p.getGrossSalary(), p.getNetSalary()});
        }
    }
}
