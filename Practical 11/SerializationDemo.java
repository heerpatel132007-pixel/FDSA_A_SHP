import java.io.*;

class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    int rollNumber;
    String name;
    transient String password;

    Student(int rollNumber, String name, String password) {
        this.rollNumber = rollNumber;
        this.name = name;
        this.password = password;
    }
}

public class SerializationDemo {

    public static void main(String[] args) {

        Student[] students = {
            new Student(1, "Rahul", "rahul123"),
            new Student(2, "Priya", "priya456"),
            new Student(3, "Amit", "amit789")
        };

       
        try {
            ObjectOutputStream output = new ObjectOutputStream(
                new FileOutputStream("students.dat"));

            output.writeObject(students);
            output.close();

            System.out.println("Objects saved successfully.");

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    
        try {
            ObjectInputStream input = new ObjectInputStream(
                new FileInputStream("students.dat"));

            Student[] loadedStudents = (Student[]) input.readObject();
            input.close();

            System.out.println("\nLoaded Student Details:");

            for (Student student : loadedStudents) {
                System.out.println("Roll Number: " + student.rollNumber);
                System.out.println("Name: " + student.name);
                System.out.println("Password: " + student.password);
                System.out.println();
            }

        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}