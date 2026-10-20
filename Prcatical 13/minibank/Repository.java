package minibank;

import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;



@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface Id {
}



public class Repository<T> {

    private HashMap<String, T> records = new HashMap<>();


    public T save(T entity) {

        try {
            for (Field field : entity.getClass().getDeclaredFields()) {

                if (field.isAnnotationPresent(Id.class)) {

                    field.setAccessible(true);

                    String id = String.valueOf(field.get(entity));

                    records.put(id, entity);

                    return entity;
                }
            }

            throw new IllegalArgumentException(
                    "ID annotation not found");

        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    public T findById(String id) {
        return records.get(id);
    }

   
    public ArrayList<T> findAll() {
        return new ArrayList<>(records.values());
    }

 
    public boolean delete(String id) {
        return records.remove(id) != null;
    }


    @SafeVarargs
    public final void saveAll(T... entities) {

        for (T entity : entities) {
            save(entity);
        }
    }


    public static <N extends Number> double average(N[] numbers) {

        double sum = 0;

        if (numbers.length == 0) {
            return 0;
        }

        for (N number : numbers) {
            sum += number.doubleValue();
        }

        return sum / numbers.length;
    }
}