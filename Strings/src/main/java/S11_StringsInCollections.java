import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

// String equality and hash codes depend on content so immutable strings are stable map keys
// equal strings must have equal hashes but different strings can have the same hash

public class S11_StringsInCollections {
    public static void main(String[] args) {
        // hash collisions do not imply equality and maps retain both distinct keys
        System.out.println("FB".hashCode() == "Ea".hashCode());	// true
        System.out.println("FB".equals("Ea"));	// false
        Map<String, Integer> values = new HashMap<>();
        values.put("FB", 1);
        values.put("Ea", 2);
        System.out.println(values.size());	// 2
        System.out.println(values.get(new String("FB")));	// 1

        // reassigning a variable does not mutate the String already stored as a key
        String key = "Java";
        values.put(key, 21);
        key += "!";
        System.out.println(values.get("Java"));	// 21
        System.out.println(values.get(key));	// null

        // hash collections use builder identity while sorted collections use its content comparison
        StringBuilder first = new StringBuilder("Java");
        StringBuilder second = new StringBuilder("Java");
        Set<StringBuilder> identities = new HashSet<>();
        identities.add(first);
        identities.add(second);
        System.out.println(identities.size());	// 2
        Set<StringBuilder> sorted = new TreeSet<>();
        sorted.add(first);
        sorted.add(second);
        System.out.println(sorted.size());	// 1

        // a case-insensitive comparator treats unequal String values as equivalent sorted keys
        Set<String> names = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        names.add("Java");
        names.add("JAVA");
        System.out.println(names);	// [Java]

        // String switch matches content rather than reference identity
        String language = new String("Java");
        String platform = switch (language) {
            case "Java" -> "JVM";
            default -> "other";
        };
        System.out.println(platform);	// JVM
    }

    // never mutate fields or contents used by ordering while an object is inside a sorted collection
    // convert builders to String snapshots when content-based immutable keys are required
    // hash codes are not unique identifiers and String hashing is not cryptographic protection
}
