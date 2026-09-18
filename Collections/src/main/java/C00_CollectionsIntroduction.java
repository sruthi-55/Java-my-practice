import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Java Collections Framework is the complete architecture of interfaces, implementations and algorithms used to store and process groups of objects
// Collection is the root interface for groups of individual elements and is extended by List, Set and Queue
// Collections is a utility class containing static algorithms such as sort(), reverse(), min() and max()
// Map is a key-value interface in the framework, but it does not extend the Collection interface
// Iterable is the parent of Collection and provides iterator(), which enables the enhanced for loop
// generics specify the allowed element type and provide compile-time type safety

public class C00_CollectionsIntroduction {
    public static void main(String[] args) {
        Collection<String> collection = new ArrayList<>();
        collection.add("Java");
        collection.add("SQL");
        System.out.println(collection);	// [Java, SQL]

        List<Integer> numbers = new ArrayList<>(List.of(30, 10, 20));
        Collections.sort(numbers);
        System.out.println(numbers);	// [10, 20, 30]

        Map<Integer, String> map = new HashMap<>();
        map.put(1, "Java");
        map.put(2, "Spring");
        System.out.println(map);	// {1=Java, 2=Spring}

        for (String value : collection) {
            System.out.println(value);	// Java then SQL
        }
    }

    // framework = interfaces + implementing classes + algorithms
    // Collection = interface representing a group of elements
    // Collections = utility class operating on collection objects
    // Map = separate interface representing key-value pairs
}
