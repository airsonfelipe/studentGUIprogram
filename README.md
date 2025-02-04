Student System with GUI interface (Swing)

My complete code:
 
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

 
 

Explanation:

The provided Java code is a comprehensive implementation of a Student Management System using the Swing framework for creating a graphical user interface (GUI). This application facilitates the management of student records, course enrollment, and grade assignments. Here's a detailed explanation of the code:

Overview
The application is built using Java Swing, which provides a set of GUI components for building desktop applications. The `StudentManagementSystem` class is the main entry point, which creates the GUI and handles various operations related to students, courses, and grades.

Components and Layout

1. JFrame: The main window of the application is a `JFrame`, which is set up with a `BorderLayout`. This layout manager allows for dividing the frame into five regions: north, south, east, west, and center. In this application, the center is used for displaying the student table, and the south is used for an output area.

2. JTable: This component displays a list of students in a tabular format. It uses a `DefaultTableModel` to manage the data. The table is initially created with columns "ID," "Name," and "Age."

3. JTextArea: The output area is a `JTextArea` where information and student details are displayed. It is placed at the bottom of the frame to show messages and outputs.

4. JMenuBar: The menu bar at the top of the window contains menus and menu items for various operations:
   - Student Menu: Contains options to add, update, and view student details.
   - Course Menu: Provides an option to enroll students in courses.
   - Grade Menu: Allows assigning grades to students.

Functionality
Initialization
In the constructor of `StudentManagementSystem`, several operations are performed:
- Data Structures: Three primary data structures are initialized:
  - `students`: A `List<Student>` that holds the student records.
  - `courses`: A `List<String>` that stores available courses.
  - `grades`: A `Map<Student, Map<String, String>>` that maps students to their course grades.
- Sample Courses: The application starts with three sample courses: Math, Science, and History.
- GUI Setup The frame is initialized, and helper methods `createMenuBar()`, `createStudentTable()`, and `createOutputArea()` are called to set up the GUI components.

Menu Bar Creation
- The `createMenuBar()` method creates and configures the menu bar. Event listeners are attached to each menu item to handle user interactions.
- For instance, selecting "Add Student" triggers `showAddStudentDialog()`, which opens a dialog to enter new student details.

Student Management
- Add Student: The `showAddStudentDialog()` method opens a dialog with text fields for entering a student's ID, name, and age. Upon confirmation, a new `Student` object is created and added to the `students` list. The `grades` map is also updated with an empty grade entry for the new student.
- Update Student: The `showUpdateStudentDialog()` allows updating an existing student's name and age. It provides a drop-down to select the student and text fields to enter new details.
- View Student Details: The `showStudentDetails()` method compiles a list of all students and displays it in the output area.

Course Enrollment
-Enroll Student: The `showEnrollStudentDialog()` presents a dialog where an administrator can select a student and a course. If the student is not already enrolled, the selected course is added to the student's entry in the `grades` map with a placeholder grade of "Not Graded."

Grade Management
- Assign Grade: The `showAssignGradeDialog()` allows assigning a grade to a student for a specific course. A drop-down is used to select the student, and another is populated with courses in which the student is enrolled. The grade is then updated in the `grades` map.

Dynamic Updates
- The `updateStudentTable()` method is called whenever there is a change in the student data. It refreshes the table display to reflect the current state of the `students` list.

Error Handling
- Throughout the application, error dialogs are shown to the user if invalid input is provided. For example, when entering non-numeric data for student ID or age, an error message is displayed.

Inner Class
- Student Class: This is a simple inner class that represents a student with an ID, name, and age. It provides getter and setter methods for these properties. The `toString()` method returns a formatted string with the student's details.

Main Method
- The `main()` method is the entry point of the application. It uses `SwingUtilities.invokeLater()` to ensure that the GUI is created and updated on the Event Dispatch Thread, which is necessary for thread safety in Swing applications.

Conclusion
The `StudentManagementSystem` is a well-organized and straightforward Java Swing application designed to manage student records, course enrollment, and grades. It demonstrates the use of Swing components, event handling, and basic data management using Java collections. The code can be extended with additional features such as search functionality, data persistence, and more complex validation to create a more comprehensive system.
