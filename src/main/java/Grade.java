// Model: one row of the table (a grade with names instead of ids)
public class Grade {

    private final int id;
    private final String student;
    private final String subject;
    private final String teacher;
    private final int score;

    public Grade(int id, String student, String subject, String teacher, int score) {
        this.id = id;
        this.student = student;
        this.subject = subject;
        this.teacher = teacher;
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public String getStudent() {
        return student;
    }

    public String getSubject() {
        return subject;
    }

    public String getTeacher() {
        return teacher;
    }

    public int getScore() {
        return score;
    }
}
