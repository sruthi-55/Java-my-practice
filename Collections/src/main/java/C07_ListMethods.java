import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;

// List preserves positional order and duplicates while mutability and null rules depend on the implementation
// a backed view shares storage while a snapshot copies the current element references

public class C07_ListMethods {
    public static void main(String[] args) {
        List<String> skills = new ArrayList<>();
        skills.add("Java");
        skills.add("SQL");
        skills.add(1, "Spring");
        skills.addAll(List.of("Git", "Java"));
        skills.addAll(2, List.of("Maven", "JUnit"));

        print("after add", skills);

        System.out.println("get: " + skills.get(1));	// get: Spring
        System.out.println("set old value: " + skills.set(1, "Spring Boot"));	// set old value: Spring
        System.out.println("contains Java: " + skills.contains("Java"));	// contains Java: true
        System.out.println("containsAll: " + skills.containsAll(List.of("Java", "SQL")));	// containsAll: true
        System.out.println("indexOf Java: " + skills.indexOf("Java"));	// indexOf Java: 0
        System.out.println("lastIndexOf Java: " + skills.lastIndexOf("Java"));	// lastIndexOf Java: 6
        System.out.println("size: " + skills.size());	// size: 7
        System.out.println("isEmpty: " + skills.isEmpty());	// isEmpty: false

        skills.remove("Git");
        System.out.println("remove index: " + skills.remove(0));	// remove index: Java
        skills.removeIf(skill -> skill.startsWith("J"));
        skills.replaceAll(String::toUpperCase);
        skills.sort(String::compareTo);

        print("after remove/replace/sort", skills);

        List<String> view = skills.subList(0, Math.min(2, skills.size()));

        System.out.println("subList view: " + view);	// subList view: [MAVEN, SPRING BOOT]
        System.out.println("array length: " + skills.toArray(String[]::new).length);	// array length: 3

        List<String> retained = new ArrayList<>(skills);
        retained.retainAll(List.of("MAVEN", "SQL"));

        print("retainAll", retained);

        retained.clear();

        System.out.println("clear then empty: " + retained.isEmpty());	// clear then empty: true
        viewsAndFactories();
    }

    static void viewsAndFactories() {
        // remove(int) selects an index while remove(Integer) selects a matching value
        List<Integer> numbers = new ArrayList<>(List.of(1, 2, 1));
        System.out.println(numbers.remove(1));	// 2
        System.out.println(numbers.remove(Integer.valueOf(1)));	// true
        System.out.println(numbers);	// [1]

        // Arrays.asList is fixed-size and backed by its array but permits replacing elements
        String[] array = {"Java", "SQL"};
        List<String> fixed = Arrays.asList(array);
        fixed.set(0, "Git");
        System.out.println(array[0]);	// Git
        try {
            fixed.add("Java");
        } catch (UnsupportedOperationException exception) {
            System.out.println(exception.getClass().getSimpleName());	// UnsupportedOperationException
        }

        // unmodifiable wrappers reflect source changes while copyOf takes an unmodifiable snapshot
        List<String> source = new ArrayList<>(List.of("Java", "SQL"));
        List<String> view = Collections.unmodifiableList(source);
        List<String> snapshot = List.copyOf(source);
        source.subList(0, 1).clear();
        System.out.println(view);	// [SQL]
        System.out.println(snapshot);	// [Java, SQL]
        try {
            List.of("Java", null);
        } catch (NullPointerException exception) {
            System.out.println(exception.getClass().getSimpleName());	// NullPointerException
        }

        // Java 21 reversed returns a backed view whose first end is the original last end
        source.addFirst("Git");
        source.addLast("Java");
        System.out.println(source.getFirst() + " " + source.getLast());	// Git Java
        source.reversed().removeFirst();
        System.out.println(source);	// [Git, SQL]

        // unmodifiable containers still share their mutable element references
        StringBuilder element = new StringBuilder("Java");
        List<StringBuilder> shallow = List.copyOf(List.of(element));
        element.append(" 21");
        System.out.println(shallow);	// [Java 21]
    }

    static void print(String label, List<String> values) {
        System.out.println(label + ": " + values);	// supplied label and current list contents
    }

    // changes through sublist effects the original
    // subList returns a backed view, so structural interference with the original list can invalidate the view

    // list.of creates an unmodifiable list while ArrayList creates a mutable resizable-array implementation
}
