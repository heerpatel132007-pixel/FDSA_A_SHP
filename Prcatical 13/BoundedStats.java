public class BoundedStats {

    public static <N extends Number> double average(N[] values) {

        if (values.length == 0) {
            throw new IllegalArgumentException(
                "Array cannot be empty"
            );
        }

        double sum = 0;

        for (N value : values) {
            sum += value.doubleValue();
        }

        return sum / values.length;
    }

    public static void main(String[] args) {

        Integer[] numbers1 = {10, 20, 30};
        Double[] numbers2 = {2.5, 3.5, 4.5};
        Long[] numbers3 = {100L, 200L, 300L};

        System.out.println("Integer Average: " + average(numbers1));
        System.out.println("Double Average: " + average(numbers2));
        System.out.println("Long Average: " + average(numbers3));
    }
}