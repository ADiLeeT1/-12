import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.util.List;

// The main window of the application (GUI)
public class Dashboard extends JFrame {

    // One style for the whole application
    private static final Font FONT = new Font("SansSerif", Font.PLAIN, 14);
    private static final Font BOLD_FONT = new Font("SansSerif", Font.BOLD, 14);
    private static final Color HEADER_COLOR = new Color(33, 64, 121);
    private static final Color BUTTON_COLOR = new Color(52, 101, 190);

    private final GradeDao dao = new GradeDao();
    private final DefaultTableModel tableModel;
    private final JTable table;

    public Dashboard() {
        // Main window: title, size, position in the center of the screen
        setTitle("University - Student Grades");
        setSize(850, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // TOP: application title
        JLabel titleLabel = new JLabel("Student Grades", JLabel.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(HEADER_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(12, 10, 12, 10));
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // CENTER: table. Column names = fields of the database
        String[] columns = {"ID", "Student", "Subject", "Teacher", "Score"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;   // cells cannot be edited directly, editing is done in the form
            }
        };
        table = new JTable(tableModel);
        table.setFont(FONT);
        table.setRowHeight(28);
        table.getTableHeader().setFont(BOLD_FONT);
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table.getColumnModel().getColumn(0).setPreferredWidth(40);    // narrow ID column
        table.getColumnModel().getColumn(4).setPreferredWidth(60);    // narrow Score column

        JScrollPane scrollPane = new JScrollPane(table);   // scrolling
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        add(scrollPane, BorderLayout.CENTER);

        // BOTTOM: buttons
        JButton addButton = createButton("Add");
        JButton editButton = createButton("Edit");
        JButton deleteButton = createButton("Delete");
        JButton refreshButton = createButton("Refresh");

        addButton.addActionListener(e -> addGrade());
        editButton.addActionListener(e -> editGrade());
        deleteButton.addActionListener(e -> deleteGrade());
        refreshButton.addActionListener(e -> loadData());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 12));
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);

        loadData();   // fill the table when the window opens
    }

    // Creates a button in the common style
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BOLD_FONT);
        button.setBackground(BUTTON_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new java.awt.Dimension(110, 36));
        return button;
    }

    // REFRESH / READ: load grades from the database into the table
    private void loadData() {
        try {
            List<Grade> grades = dao.getAllGrades();
            tableModel.setRowCount(0);   // remove old rows
            for (Grade grade : grades) {
                tableModel.addRow(new Object[]{
                        grade.getId(),
                        grade.getStudent(),
                        grade.getSubject(),
                        grade.getTeacher(),
                        grade.getScore()
                });
            }
        } catch (SQLException e) {
            showError("Could not load data: " + e.getMessage());
        }
    }

    // ADD: form with two drop-down lists and a score field
    private void addGrade() {
        try {
            JComboBox<Item> studentBox = new JComboBox<>(dao.getStudents().toArray(new Item[0]));
            JComboBox<Item> subjectBox = new JComboBox<>(dao.getSubjects().toArray(new Item[0]));
            JTextField scoreField = new JTextField();

            JPanel form = createForm();
            form.add(createLabel("Student:"));
            form.add(studentBox);
            form.add(createLabel("Subject:"));
            form.add(subjectBox);
            form.add(createLabel("Score (0-100):"));
            form.add(scoreField);

            int result = JOptionPane.showConfirmDialog(this, form, "Add grade",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result != JOptionPane.OK_OPTION) {
                return;   // user pressed Cancel
            }

            Item student = (Item) studentBox.getSelectedItem();
            Item subject = (Item) subjectBox.getSelectedItem();
            if (student == null || subject == null) {
                showError("There are no students or subjects in the database.");
                return;
            }

            int score = parseScore(scoreField.getText());   // checks the value
            dao.addGrade(student.getId(), subject.getId(), score);
            loadData();

        } catch (IllegalArgumentException e) {
            showError(e.getMessage());   // wrong score
        } catch (SQLException e) {
            showError("Database error: " + e.getMessage());
        }
    }

    // EDIT: the form is filled with the current data of the selected row
    private void editGrade() {
        int row = table.getSelectedRow();
        if (row == -1) {
            showError("Please select a row in the table first.");
            return;
        }

        int id = (int) table.getValueAt(row, 0);
        String student = (String) table.getValueAt(row, 1);
        String subject = (String) table.getValueAt(row, 2);
        int oldScore = (int) table.getValueAt(row, 4);

        JTextField scoreField = new JTextField(String.valueOf(oldScore));

        JPanel form = createForm();
        form.add(createLabel("Student:"));
        form.add(createLabel(student));
        form.add(createLabel("Subject:"));
        form.add(createLabel(subject));
        form.add(createLabel("Score (0-100):"));
        form.add(scoreField);

        int result = JOptionPane.showConfirmDialog(this, form, "Edit grade",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            int newScore = parseScore(scoreField.getText());
            if (!dao.updateGrade(id, newScore)) {
                showError("This grade no longer exists.");
            }
            loadData();

        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        } catch (SQLException e) {
            showError("Database error: " + e.getMessage());
        }
    }

    // DELETE: ask for confirmation, then delete the selected row
    private void deleteGrade() {
        int row = table.getSelectedRow();
        if (row == -1) {
            showError("Please select a row in the table first.");
            return;
        }

        int id = (int) table.getValueAt(row, 0);

        int answer = JOptionPane.showConfirmDialog(this,
                "Delete the selected grade?", "Confirm delete",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (answer != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            if (!dao.deleteGrade(id)) {
                showError("This grade no longer exists.");
            }
            loadData();
        } catch (SQLException e) {
            showError("Database error: " + e.getMessage());
        }
    }

    // Validation: the score must be a whole number from 0 to 100
    static int parseScore(String text) {
        String value = text.trim();
        if (value.isEmpty()) {
            throw new IllegalArgumentException("Score is required.");
        }

        int score;
        try {
            score = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Score must be a whole number.");
        }

        if (score < 0 || score > 100) {
            throw new IllegalArgumentException("Score must be between 0 and 100.");
        }
        return score;
    }

    // Panel for the forms: 2 columns (label + field), spaces between elements
    private JPanel createForm() {
        JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return form;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT);
        return label;
    }

    // Error message window
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
