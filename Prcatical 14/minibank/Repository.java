package minibank;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface Id {
}

class Repository<T> {

    private final ConcurrentHashMap<String, T> records =
            new ConcurrentHashMap<>();

    public void save(T object) {

        try {
            Field[] fields = object.getClass().getDeclaredFields();

            for (Field field : fields) {

                if (field.isAnnotationPresent(Id.class)) {
                    field.setAccessible(true);

                    Object idValue = field.get(object);

                    records.put(String.valueOf(idValue), object);

                    return;
                }
            }

            throw new IllegalArgumentException(
                    "No field with @Id annotation found.");

        } catch (IllegalAccessException exception) {
            throw new RuntimeException(
                    "Unable to save object.", exception);
        }
    }

    public T findById(String id) {
        return records.get(id);
    }

    public Collection<T> findAll() {
        return new ArrayList<>(records.values());
    }

    public boolean delete(String id) {
        return records.remove(id) != null;
    }

    public void clear() {
        records.clear();
    }

    @SafeVarargs
    public final void saveAll(T... objects) {
        for (T object : objects) {
            save(object);
        }
    }

    public int count() {
        return records.size();
    }

    public static <N extends Number> double average(N[] numbers) {

        if (numbers == null || numbers.length == 0) {
            return 0;
        }

        double total = 0;

        for (N number : numbers) {
            total += number.doubleValue();
        }

        return total / numbers.length;
    }
}