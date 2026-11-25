import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// All work with the "grades" table (CRUD).
// The SQL queries are the same as before. Now the methods RETURN data
// (instead of printing it), so the GUI can show it in a table.
// SQLException is passed to the GUI, which shows it to the user in a message window.
public class GradeDao {

    // READ: all grades (grade + student + subject + teacher)
    public List<Grade> getAllGrades() throws SQLException {
        String sql = "SELECT g.id, s.name AS student, sub.title AS subject, t.name AS teacher, g.score "
                + "FROM grades g "
                + "JOIN students s ON g.student_id = s.id "
                + "JOIN subjects sub ON g.subject_id = sub.id "
                + "JOIN teachers t ON sub.teacher_id = t.id "
                + "ORDER BY g.id";

        List<Grade> grades = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                grades.add(new Grade(
                        rs.getInt("id"),
                        rs.getString("student"),
                        rs.getString("subject"),
                        rs.getString("teacher"),
                        rs.getInt("score")));
            }
        }
        return grades;
    }

    // READ: students for the drop-down list
    public List<Item> getStudents() throws SQLException {
        String sql = "SELECT id, name FROM students ORDER BY id";
        List<Item> items = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                items.add(new Item(rs.getInt("id"), rs.getString("name")));
            }
        }
        return items;
    }

    // READ: subjects for the drop-down list
    public List<Item> getSubjects() throws SQLException {
        String sql = "SELECT id, title FROM subjects ORDER BY id";
        List<Item> items = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                items.add(new Item(rs.getInt("id"), rs.getString("title")));
            }
        }
        return items;
    }

    // CREATE: add a new grade
    public void addGrade(int studentId, int subjectId, int score) throws SQLException {
        String sql = "INSERT INTO grades (student_id, subject_id, score) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ps.setInt(2, subjectId);
            ps.setInt(3, score);
            ps.executeUpdate();
        }
    }

    // UPDATE: change the score of a grade. Returns true if the grade was found.
    public boolean updateGrade(int id, int newScore) throws SQLException {
        String sql = "UPDATE grades SET score = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, newScore);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        }
    }

    // DELETE: remove a grade. Returns true if the grade was found.
    public boolean deleteGrade(int id) throws SQLException {
        String sql = "DELETE FROM grades WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}
