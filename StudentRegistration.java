import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class StudentRegistration extends JFrame {

    private JTextField txtId;
    private JTextField txtName;
    private JTextField txtDepartment;
    private JTextField txtEmail;

    private JButton btnAdd;
    private JButton btnView;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;

    private Connection con;

    public StudentRegistration() {

        setTitle("Student Registration System");
        setSize(650, 500);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JLabel title = new JLabel("Student Registration System");
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setBounds(150, 30, 400, 40);
        add(title);

        JLabel lblId = new JLabel("Student ID");
        lblId.setFont(new Font("Arial", Font.PLAIN, 16));
        lblId.setBounds(100, 100, 120, 30);
        add(lblId);

        txtId = new JTextField();
        txtId.setBounds(250, 100, 250, 30);
        add(txtId);

        JLabel lblName = new JLabel("Student Name");
        lblName.setFont(new Font("Arial", Font.PLAIN, 16));
        lblName.setBounds(100, 150, 120, 30);
        add(lblName);

        txtName = new JTextField();
        txtName.setBounds(250, 150, 250, 30);
        add(txtName);

        JLabel lblDepartment = new JLabel("Department");
        lblDepartment.setFont(new Font("Arial", Font.PLAIN, 16));
        lblDepartment.setBounds(100, 200, 120, 30);
        add(lblDepartment);

        txtDepartment = new JTextField();
        txtDepartment.setBounds(250, 200, 250, 30);
        add(txtDepartment);

        JLabel lblEmail = new JLabel("Email");
        lblEmail.setFont(new Font("Arial", Font.PLAIN, 16));
        lblEmail.setBounds(100, 250, 120, 30);
        add(lblEmail);

        txtEmail = new JTextField();
        txtEmail.setBounds(250, 250, 250, 30);
        add(txtEmail);

        btnAdd = new JButton("Add");
        btnAdd.setBounds(40, 330, 100, 40);
        add(btnAdd);

        btnView = new JButton("View");
        btnView.setBounds(150, 330, 100, 40);
        add(btnView);

        btnUpdate = new JButton("Update");
        btnUpdate.setBounds(260, 330, 100, 40);
        add(btnUpdate);

        btnDelete = new JButton("Delete");
        btnDelete.setBounds(370, 330, 100, 40);
        add(btnDelete);

        btnClear = new JButton("Clear");
        btnClear.setBounds(480, 330, 100, 40);
        add(btnClear);

        connectDatabase();

        btnAdd.addActionListener(e -> addStudent());
        btnView.addActionListener(e -> viewStudent());
        btnUpdate.addActionListener(e -> updateStudent());
        btnDelete.addActionListener(e -> deleteStudent());
        btnClear.addActionListener(e -> clearFields());

        setVisible(true);
    }

    // DATABASE CONNECTION
    private void connectDatabase() {

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/student_management",
                "studentapp",
                "StudentApp@2026"
            );

            JOptionPane.showMessageDialog(
                this,
                "Database Connected Successfully"
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                this,
                "Database Connection Failed\n" + e.getMessage()
            );

            e.printStackTrace();
        }
    }

    // ADD STUDENT
    private void addStudent() {

        if (txtId.getText().trim().isEmpty()
                || txtName.getText().trim().isEmpty()
                || txtDepartment.getText().trim().isEmpty()
                || txtEmail.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                this,
                "Please fill all fields"
            );

            return;
        }

        try {

            int studentId = Integer.parseInt(
                txtId.getText().trim()
            );

            String sql =
                "INSERT INTO students " +
                "(student_id, student_name, department, email) " +
                "VALUES (?, ?, ?, ?)";

            PreparedStatement ps =
                con.prepareStatement(sql);

            ps.setInt(1, studentId);
            ps.setString(2, txtName.getText().trim());
            ps.setString(3, txtDepartment.getText().trim());
            ps.setString(4, txtEmail.getText().trim());

            ps.executeUpdate();

            JOptionPane.showMessageDialog(
                this,
                "Student Added Successfully"
            );

            ps.close();

            clearFields();

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                this,
                "Student ID must be a number"
            );

        } catch (SQLIntegrityConstraintViolationException e) {

            JOptionPane.showMessageDialog(
                this,
                "Student ID already exists"
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                this,
                "Error: " + e.getMessage()
            );

            e.printStackTrace();
        }
    }

    // VIEW STUDENT
    private void viewStudent() {

        if (txtId.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                this,
                "Enter Student ID"
            );

            return;
        }

        try {

            int studentId =
                Integer.parseInt(txtId.getText().trim());

            String sql =
                "SELECT * FROM students WHERE student_id = ?";

            PreparedStatement ps =
                con.prepareStatement(sql);

            ps.setInt(1, studentId);

            ResultSet rs =
                ps.executeQuery();

            if (rs.next()) {

                txtName.setText(
                    rs.getString("student_name")
                );

                txtDepartment.setText(
                    rs.getString("department")
                );

                txtEmail.setText(
                    rs.getString("email")
                );

            } else {

                JOptionPane.showMessageDialog(
                    this,
                    "Student Not Found"
                );
            }

            rs.close();
            ps.close();

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                this,
                "Student ID must be a number"
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                this,
                "Error: " + e.getMessage()
            );

            e.printStackTrace();
        }
    }

    // UPDATE STUDENT
    private void updateStudent() {

        if (txtId.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                this,
                "Enter Student ID"
            );

            return;
        }

        try {

            int studentId =
                Integer.parseInt(txtId.getText().trim());

            String sql =
                "UPDATE students " +
                "SET student_name = ?, department = ?, email = ? " +
                "WHERE student_id = ?";

            PreparedStatement ps =
                con.prepareStatement(sql);

            ps.setString(
                1,
                txtName.getText().trim()
            );

            ps.setString(
                2,
                txtDepartment.getText().trim()
            );

            ps.setString(
                3,
                txtEmail.getText().trim()
            );

            ps.setInt(
                4,
                studentId
            );

            int rows =
                ps.executeUpdate();

            if (rows > 0) {

                JOptionPane.showMessageDialog(
                    this,
                    "Student Updated Successfully"
                );

            } else {

                JOptionPane.showMessageDialog(
                    this,
                    "Student Not Found"
                );
            }

            ps.close();

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                this,
                "Student ID must be a number"
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                this,
                "Error: " + e.getMessage()
            );

            e.printStackTrace();
        }
    }

    // DELETE STUDENT
    private void deleteStudent() {

        if (txtId.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                this,
                "Enter Student ID"
            );

            return;
        }

        try {

            int studentId =
                Integer.parseInt(txtId.getText().trim());

            int confirm =
                JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this student?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
                );

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            String sql =
                "DELETE FROM students WHERE student_id = ?";

            PreparedStatement ps =
                con.prepareStatement(sql);

            ps.setInt(
                1,
                studentId
            );

            int rows =
                ps.executeUpdate();

            if (rows > 0) {

                JOptionPane.showMessageDialog(
                    this,
                    "Student Deleted Successfully"
                );

                clearFields();

            } else {

                JOptionPane.showMessageDialog(
                    this,
                    "Student Not Found"
                );
            }

            ps.close();

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                this,
                "Student ID must be a number"
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                this,
                "Error: " + e.getMessage()
            );

            e.printStackTrace();
        }
    }

    // CLEAR TEXT FIELDS
    private void clearFields() {

        txtId.setText("");
        txtName.setText("");
        txtDepartment.setText("");
        txtEmail.setText("");

        txtId.requestFocus();
    }

    // MAIN METHOD
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new StudentRegistration();
        });
    }
}