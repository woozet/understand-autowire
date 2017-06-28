import com.woozet.components.DITarget;
import com.woozet.components.annotations.Autowired;
import com.woozet.components.annotations.Name;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static ArrayList<Object> instances = new ArrayList<>();

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        // mocking component scan - reflection
        Class classDITarget = Class.forName("com.woozet.components.DITarget");
        Class classTestClass = Class.forName("com.woozet.components.ClassWhichHasName");

        // make singleton beans
        Object instanceDITarget = classDITarget.newInstance();
        Object instanceTestClass = classTestClass.newInstance();

        // add into list to manage beans
        instances.add(instanceTestClass);
        instances.add(instanceDITarget);

        // DI and annotation processing
        instances.stream()
                .map(Main::injectNameAnnotationValue)
                .map(Main::injectAutowiredInstance)
                .collect(Collectors.toList());

        // test
        DITarget instance = (DITarget)instances.get(1);
        instance.printPrettyName();
    }

    private static Object injectNameAnnotationValue(Object instanceTestClass) {
        try {
            Name n = instanceTestClass.getClass().getAnnotation(Name.class);
            
            if (n != null) {
                Field name = instanceTestClass.getClass().getDeclaredField("name");
                name.setAccessible(true);
                name.set(instanceTestClass, n.value());
            }
        } catch (NoSuchFieldException|IllegalAccessException e) {
            System.out.println("Not found name field but class has annotation.");
        }

        return instanceTestClass;
    }

    private static Object injectAutowiredInstance(Object instance) {
        List<Field> fields = Arrays.asList(instance.getClass().getDeclaredFields());

        fields.forEach(field -> {
            if (field.getAnnotation(Autowired.class) != null) {
                try {
                    List<Object> matched = instances.stream()
                            .filter(i -> i.getClass().getName().equals(field.getType().getName()))
                            .collect(Collectors.toList());

                    if (matched.size() == 1) {
                        field.setAccessible(true);
                        field.set(instance, matched.get(0));
                    } else {
                        System.out.println("There is no suitable type of instance (duplicated or not found) for class " + field.getType().getName());
                    }
                } catch (IllegalAccessException e) {
                    System.out.println("Making instance failed");
                }
            }
        });

        return instance;
    }
}
