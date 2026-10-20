class Stack<T> {

    private Object[] elements;
    private int top;

    public Stack(int size) {
        elements = new Object[size];
        top = -1;
    }

    public void push(T value) {

        if (top == elements.length - 1) {
            System.out.println("Stack is full");
            return;
        }

        elements[++top] = value;
    }

    @SuppressWarnings("unchecked")
    public T pop() {

        if (top == -1) {
            System.out.println("Stack is empty");
            return null;
        }

        T value = (T) elements[top];
        elements[top] = null;
        top--;

        return value;
    }

    public void display() {

        if (top == -1) {
            System.out.println("Stack is empty");
            return;
        }

        for (int i = top; i >= 0; i--) {
            System.out.println(elements[i]);
        }
    }
}

public class GenericStackDemo {

    public static void main(String[] args) {

        Stack<Integer> numberStack = new Stack<>(5);

        numberStack.push(10);
        numberStack.push(20);
        numberStack.push(30);

        System.out.println("Integer Stack:");
        numberStack.display();

        System.out.println("Popped: " + numberStack.pop());

        Stack<String> nameStack = new Stack<>(5);

        nameStack.push("Rahul");
        nameStack.push("Priya");

        System.out.println("\nString Stack:");
        nameStack.display();

        System.out.println("Popped: " + nameStack.pop());

        Stack<Integer> emptyStack = new Stack<>(3);

        System.out.println("\nEmpty Stack Test:");
        emptyStack.pop();
    }
}