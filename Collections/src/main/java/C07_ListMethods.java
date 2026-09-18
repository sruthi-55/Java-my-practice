import java.util.ArrayList;
import java.util.List;

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
    }

    static void print(String label, List<String> values) {
        System.out.println(label + ": " + values);	// supplied label and current list contents
    }

    // changes through sublist effects the original
    // subList returns a backed view, so structural interference with the original list can invalidate the view

    // list.of creates an unmodifiable list while ArrayList creates a mutable resizable-array implementation
}
