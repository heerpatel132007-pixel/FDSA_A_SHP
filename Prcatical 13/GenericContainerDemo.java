class Box<T> {

    private T value;

    public void set(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }
}

class Pair<A, B> {

    private A first;
    private B second;

    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public A getFirst() {
        return first;
    }

    public B getSecond() {
        return second;
    }
}

public class GenericContainerDemo {

    // Generic method
    public static <T> T getFirst(T[] array) {
        return array[0];
    }

    public static void main(String[] args) {

        Box<String> stringBox = new Box<>();
        stringBox.set("Hello Java");

        Box<Integer> integerBox = new Box<>();
        integerBox.set(100);

        System.out.println("String Box: " + stringBox.get());
        System.out.println("Integer Box: " + integerBox.get());

        Pair<String, Integer> student =
            new Pair<>("Rahul", 20);

        System.out.println("Name: " + student.getFirst());
        System.out.println("Age: " + student.getSecond());

        String[] names = {"Priya", "Amit", "Neha"};
        Integer[] numbers = {10, 20, 30};

        System.out.println("First Name: " + getFirst(names));
        System.out.println("First Number: " + getFirst(numbers));
    }
}