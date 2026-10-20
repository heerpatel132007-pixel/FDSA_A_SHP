import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

class Student implements Serializable {

    private static final long serialVersionUID = 1L;

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

    @Override
    public boolean equals(Object object) {

        if (this == object) {
            return true;
        }

        if (!(object instanceof Student)) {
            return false;
        }

        Student other = (Student) object;

        return studentId == other.studentId
                && Objects.equals(studentName, other.studentName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, studentName);
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

    public ArrayList<Student> getStudents() {
        return new ArrayList<>(students);
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

   
    public void saveToFile(String fileName) throws IOException {

        try (ObjectOutputStream output =
                     new ObjectOutputStream(
                             new FileOutputStream(fileName))) {

            output.writeObject(students);
        }
    }

    
    public void loadFromFile(String fileName)
            throws IOException, ClassNotFoundException {

        try (ObjectInputStream input =
                     new ObjectInputStream(
                             new FileInputStream(fileName))) {

            Object savedObject = input.readObject();

            if (!(savedObject instanceof ArrayList<?>)) {
                throw new IOException("Invalid student data file.");
            }

            ArrayList<?> savedList = (ArrayList<?>) savedObject;
            ArrayList<Student> loadedStudents = new ArrayList<>();

            for (Object object : savedList) {

                if (!(object instanceof Student)) {
                    throw new IOException("Invalid student record.");
                }

                loadedStudents.add((Student) object);
            }

            students = loadedStudents;
        }
    }

   
    public boolean testSaveAndLoad(String fileName)
            throws IOException, ClassNotFoundException {

        ArrayList<Student> originalStudents = getStudents();

        saveToFile(fileName);

        StudentService newService = new StudentService();
        newService.loadFromFile(fileName);

        return originalStudents.equals(newService.getStudents());
    }
}


public class StudentApp1 {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        StudentService service = new StudentService();

        String fileName = "students.dat";

        while (true) {

            System.out.println("\n--- Student Management System ---");
            System.out.println("1. Add Student");
            System.out.println("2. Display Students");
            System.out.println("3. Save Students");
            System.out.println("4. Load Students");
            System.out.println("5. Test Save and Load");
            System.out.println("6. Exit");
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
                    try {
                        service.saveToFile(fileName);
                        System.out.println("Students saved successfully.");
                    } catch (IOException exception) {
                        System.out.println(
                                "Error saving students: "
                                        + exception.getMessage());
                    }
                    break;

                case 4:
                    try {
                        service.loadFromFile(fileName);
                        System.out.println("Students loaded successfully.");
                    } catch (IOException | ClassNotFoundException exception) {
                        System.out.println(
                                "Error loading students: "
                                        + exception.getMessage());
                    }
                    break;

                case 5:
                    try {
                        if (service.testSaveAndLoad(fileName)) {
                            System.out.println(
                                    "Test Passed: Data saved and loaded unchanged.");
                        } else {
                            System.out.println(
                                    "Test Failed: Data has changed.");
                        }
                    } catch (IOException | ClassNotFoundException exception) {
                        System.out.println(
                                "Test Failed: " + exception.getMessage());
                    }
                    break;

                case 6:
                    System.out.println("Exiting application.");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid menu choice.");
            }
        }
    }
}