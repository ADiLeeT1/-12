# University — GUI for the CRUD application (Swing)

Graphical interface (Java Swing) for the CRUD application "Student Grades".
Uses the same MySQL database `university_db` (script `schema.sql`).

## Structure
- `src/main/java/Main.java` — starts the application (opens the window)
- `src/main/java/DBConnection.java` — connection to the database
- `src/main/java/Grade.java` — model: one row of the table
- `src/main/java/Item.java` — model: id + text for the drop-down lists
- `src/main/java/GradeDao.java` — CRUD operations (SQL with `PreparedStatement`, try-with-resources)
- `src/main/java/Dashboard.java` — the window: `JFrame`, `JTable`, buttons, forms
- `pom.xml` — Maven, dependency `mysql-connector-j` (Swing is part of the JDK)
- `schema.sql` — database script

## How to run
1. Run `schema.sql` in MySQL Workbench (creates the database `university_db` with test data).
2. In `DBConnection.java` put your MySQL password.
3. Open the folder in IntelliJ IDEA (imports `pom.xml`), run `Main`.

## GUI requirements
| Requirement | Where |
|---|---|
| `JFrame`: title, size, center of the screen | constructor of `Dashboard` (`setTitle`, `setSize`, `setLocationRelativeTo(null)`) |
| Panels and layout managers | `BorderLayout` (window), `FlowLayout` (buttons), `GridLayout` (forms) |
| `JTable` + `DefaultTableModel`, columns = DB fields | `ID, Student, Subject, Teacher, Score` |
| Cells cannot be edited directly | `isCellEditable` returns `false` |
| `JScrollPane` | table is placed in a scroll pane |
| Buttons Add / Edit / Delete / Refresh | bottom panel |
| Add: form + DAO method | `addGrade()` -> `dao.addGrade(...)` |
| Edit: form filled with current data | `editGrade()` -> `dao.updateGrade(...)` |
| Delete with confirmation | `deleteGrade()`: `JOptionPane` YES/NO |
| Refresh: reload from the database | `loadData()` |
| Validation + clear error messages | `parseScore()`, `showError()` |
| One style (colors, fonts, spaces) | constants `FONT`, `BOLD_FONT`, `HEADER_COLOR`, `BUTTON_COLOR` |
