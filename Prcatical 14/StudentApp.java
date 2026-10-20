import java.util.ArrayList;
import java.util.Scanner;


class Student {

    private int studentId;
    private String studentName;

    public Student(int studentId, String studentName) {
        this.studentId = studentId;
        this.studentName = studentName;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    @Override
    public String toString() {
        return "Student ID: " + studentId
                + ", Name: " + studentName;
    }
}


class StudentService {

    private ArrayList<Student> students = new ArrayList<>();

    public void addStudent(Student student) {
        students.add(student);
    }

    public int getStudentCount() {
        return students.size();
    }

    public void displayStudents() {
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }

        for (Student student : students) {
            System.out.println(student);
        }
    }

    public void performAction(String action) {
        if (action.equalsIgnoreCase("add")) {
            System.out.println("Add action selected.");
        } else if (action.equalsIgnoreCase("display")) {
            displayStudents();
        } else {
            throw new IllegalArgumentException(
                    "Invalid action: " + action);
        }
    }
}


public class StudentApp {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        StudentService service = new StudentService();

        while (true) {
            System.out.println("\n--- Student Management System ---");
            System.out.println("1. Add Student");
            System.out.println("2. Display Students");
            System.out.println("3. Test Student Count");
            System.out.println("4. Test Invalid Action");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1:
                    System.out.print("Enter student ID: ");
                    int studentId = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Enter student name: ");
                    String studentName = scanner.nextLine();

                    service.addStudent(
                            new Student(studentId, studentName));

                    System.out.println("Student added successfully.");
                    break;

                case 2:
                    service.displayStudents();
                    break;

                case 3:
                    int oldCount = service.getStudentCount();

                    service.addStudent(
                            new Student(999, "Test Student"));

                    int newCount = service.getStudentCount();

                    if (newCount == oldCount + 1) {
                        System.out.println(
                                "Test Passed: Student count increased.");
                    } else {
                        System.out.println("Test Failed.");
                    }
                    break;

                case 4:
                    try {
                        service.performAction("delete");
                        System.out.println("Test Failed.");
                    } catch (IllegalArgumentException exception) {
                        System.out.println(
                                "Test Passed: Invalid action throws exception.");
                    }
                    break;

                case 5:
                    System.out.println("Exiting application.");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid menu choice.");
            }
        }
    }
}