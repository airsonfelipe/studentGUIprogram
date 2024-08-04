import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentManagementSystem {
    private JFrame frame;
    private JTable studentTable;
    private DefaultTableModel studentTableModel;
    private JTextArea outputArea;

    // Data structures to store students, courses, and grades
    private List<Student> students;
    private List<String> courses;
    private Map<Student, Map<String, String>> grades;

    public StudentManagementSystem() {
        students = new ArrayList<>();
        courses = new ArrayList<>();
        grades = new HashMap<>();

        // Sample courses
        courses.add("Math");
        courses.add("Science");
        courses.add("History");

        // Create the main frame
        frame = new JFrame("Student Management System");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Create components
        createMenuBar();
        createStudentTable();
        createOutputArea();

        // Display the frame
        frame.setVisible(true);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu studentMenu = new JMenu("Student");
        JMenuItem addStudent = new JMenuItem("Add Student");
        JMenuItem updateStudent = new JMenuItem("Update Student");
        JMenuItem viewStudentDetails = new JMenuItem("View Student Details");

        addStudent.addActionListener(e -> showAddStudentDialog());
        updateStudent.addActionListener(e -> showUpdateStudentDialog());
        viewStudentDetails.addActionListener(e -> showStudentDetails());

        studentMenu.add(addStudent);
        studentMenu.add(updateStudent);
        studentMenu.add(viewStudentDetails);

        JMenu courseMenu = new JMenu("Course");
        JMenuItem enrollCourse = new JMenuItem("Enroll in Course");
        enrollCourse.addActionListener(e -> showEnrollStudentDialog());

        courseMenu.add(enrollCourse);

        JMenu gradeMenu = new JMenu("Grade");
        JMenuItem assignGrade = new JMenuItem("Assign Grade");
        assignGrade.addActionListener(e -> showAssignGradeDialog());

        gradeMenu.add(assignGrade);

        menuBar.add(studentMenu);
        menuBar.add(courseMenu);
        menuBar.add(gradeMenu);

        frame.setJMenuBar(menuBar);
    }

    private void createStudentTable() {
        String[] columnNames = {"ID", "Name", "Age"};
        studentTableModel = new DefaultTableModel(columnNames, 0);
        studentTable = new JTable(studentTableModel);
        JScrollPane scrollPane = new JScrollPane(studentTable);
        frame.add(scrollPane, BorderLayout.CENTER);
    }

    private void createOutputArea() {
        outputArea = new JTextArea(5, 20);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        frame.add(scrollPane, BorderLayout.SOUTH);
    }

    private void showAddStudentDialog() {
        JTextField idField = new JTextField(5);
        JTextField nameField = new JTextField(10);
        JTextField ageField = new JTextField(3);

        JPanel panel = new JPanel();
        panel.add(new JLabel("ID:"));
        panel.add(idField);
        panel.add(Box.createHorizontalStrut(15));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(Box.createHorizontalStrut(15));
        panel.add(new JLabel("Age:"));
        panel.add(ageField);

        int result = JOptionPane.showConfirmDialog(frame, panel,
                "Add New Student", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                int id = Integer.parseInt(idField.getText());
                String name = nameField.getText();
                int age = Integer.parseInt(ageField.getText());

                Student student = new Student(id, name, age);
                students.add(student);
                grades.put(student, new HashMap<>());
                updateStudentTable();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Invalid input! Please enter valid numbers for ID and Age.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showUpdateStudentDialog() {
        String[] studentNames = students.stream().map(Student::getName).toArray(String[]::new);
        JComboBox<String> studentList = new JComboBox<>(studentNames);

        JTextField nameField = new JTextField(10);
        JTextField ageField = new JTextField(3);

        JPanel panel = new JPanel();
        panel.add(new JLabel("Select Student:"));
        panel.add(studentList);
        panel.add(Box.createHorizontalStrut(15));
        panel.add(new JLabel("New Name:"));
        panel.add(nameField);
        panel.add(Box.createHorizontalStrut(15));
        panel.add(new JLabel("New Age:"));
        panel.add(ageField);

        int result = JOptionPane.showConfirmDialog(frame, panel,
                "Update Student Information", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                Student selectedStudent = students.get(studentList.getSelectedIndex());
                String newName = nameField.getText();
                int newAge = Integer.parseInt(ageField.getText());

                selectedStudent.setName(newName);
                selectedStudent.setAge(newAge);
                updateStudentTable();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Invalid input! Please enter a valid number for Age.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showStudentDetails() {
        StringBuilder details = new StringBuilder();
        for (Student student : students) {
            details.append(student).append("\n");
        }
        outputArea.setText(details.toString());
    }

    private void showEnrollStudentDialog() {
        String[] studentNames = students.stream().map(Student::getName).toArray(String[]::new);
        String[] courseArray = courses.toArray(new String[0]);

        JComboBox<String> studentList = new JComboBox<>(studentNames);
        JComboBox<String> courseList = new JComboBox<>(courseArray);

        JPanel panel = new JPanel();
        panel.add(new JLabel("Select Student:"));
        panel.add(studentList);
        panel.add(Box.createHorizontalStrut(15));
        panel.add(new JLabel("Select Course:"));
        panel.add(courseList);

        int result = JOptionPane.showConfirmDialog(frame, panel,
                "Enroll Student in Course", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            Student selectedStudent = students.get(studentList.getSelectedIndex());
            String selectedCourse = (String) courseList.getSelectedItem();
            Map<String, String> studentGrades = grades.get(selectedStudent);
            if (studentGrades.containsKey(selectedCourse)) {
                JOptionPane.showMessageDialog(frame, "Student is already enrolled in this course.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                studentGrades.put(selectedCourse, "Not Graded");
                JOptionPane.showMessageDialog(frame, "Student enrolled in " + selectedCourse);
            }
        }
    }

    private void showAssignGradeDialog() {
        String[] studentNames = students.stream().map(Student::getName).toArray(String[]::new);

        JComboBox<String> studentList = new JComboBox<>(studentNames);

        studentList.addActionListener(e -> {
            int selectedStudentIndex = studentList.getSelectedIndex();
            Student selectedStudent = students.get(selectedStudentIndex);
            Map<String, String> studentGrades = grades.get(selectedStudent);
            String[] enrolledCourses = studentGrades.keySet().toArray(new String[0]);
            JComboBox<String> courseList = new JComboBox<>(enrolledCourses);

            JTextField gradeField = new JTextField(5);

            JPanel panel = new JPanel();
            panel.add(new JLabel("Select Student:"));
            panel.add(studentList);
            panel.add(Box.createHorizontalStrut(15));
            panel.add(new JLabel("Select Course:"));
            panel.add(courseList);
            panel.add(Box.createHorizontalStrut(15));
            panel.add(new JLabel("Assign Grade:"));
            panel.add(gradeField);

            int result = JOptionPane.showConfirmDialog(frame, panel,
                    "Assign Grade to Student", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                String selectedCourse = (String) courseList.getSelectedItem();
                String grade = gradeField.getText();
                studentGrades.put(selectedCourse, grade);
                JOptionPane.showMessageDialog(frame, "Grade assigned: " + grade);
            }
        });

        JPanel panel = new JPanel();
        panel.add(new JLabel("Select Student:"));
        panel.add(studentList);

        JOptionPane.showMessageDialog(frame, panel, "Assign Grade", JOptionPane.PLAIN_MESSAGE);
    }

    private void updateStudentTable() {
        studentTableModel.setRowCount(0); // Clear the table
        for (Student student : students) {
            studentTableModel.addRow(new Object[]{student.getId(), student.getName(), student.getAge()});
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentManagementSystem::new);
    }

    // Inner class to represent a Student
    static class Student {
        private int id;
        private String name;
        private int age;

        public Student(int id, String name, int age) {
            this.id = id;
            this.name = name;
            this.age = age;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "Student ID: " + id + ", Name: " + name + ", Age: " + age;
        }
    }
}
