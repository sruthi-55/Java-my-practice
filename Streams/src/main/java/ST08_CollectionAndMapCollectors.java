import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// collect performs mutable reduction using a Collector or explicit supplier, accumulator and combiner
// Stream.toList is unmodifiable while Collectors.toList does not specify the result's mutability or concrete type

public class ST08_CollectionAndMapCollectors {
    public static void main(String[] args) {
        List<String> source = List.of("Java", "SQL", "Java");
        System.out.println(source.stream().collect(Collectors.toList()));	// [Java, SQL, Java]
        System.out.println(new TreeSet<>(source.stream().collect(Collectors.toSet())));	// [Java, SQL]
        LinkedHashSet<String> ordered = source.stream().collect(Collectors.toCollection(LinkedHashSet::new));
        System.out.println(ordered);	// [Java, SQL]
        ArrayList<String> mutable = source.stream().collect(Collectors.toCollection(ArrayList::new));
        mutable.add("Git");
        System.out.println(mutable);	// [Java, SQL, Java, Git]
        System.out.println(source.stream().collect(Collectors.joining(", ", "[", "]")));	// [Java, SQL, Java]

        // explicitly request an unmodifiable result when callers must not structurally change it
        List<String> unmodifiable = source.stream().toList();
        try {
            unmodifiable.add("Git");
        } catch (UnsupportedOperationException exception) {
            System.out.println(exception.getClass().getSimpleName());	// UnsupportedOperationException
        }
        System.out.println(source.stream().collect(Collectors.toUnmodifiableList()));	// [Java, SQL, Java]
        System.out.println(new TreeSet<>(source.stream().collect(Collectors.toUnmodifiableSet())));	// [Java, SQL]

        // toList permits null elements but toUnmodifiableList rejects them
        System.out.println(Stream.of("Java", null).toList());	// [Java, null]
        try {
            Stream.of("Java", null).collect(Collectors.toUnmodifiableList());
        } catch (NullPointerException exception) {
            System.out.println(exception.getClass().getSimpleName());	// NullPointerException
        }

        // toMap requires a merge function when multiple elements produce the same key
        try {
            source.stream().collect(Collectors.toMap(Function.identity(), String::length));
        } catch (IllegalStateException exception) {
            System.out.println("duplicate key");	// duplicate key
        }
        Map<String, Integer> lengths = source.stream().collect(Collectors.toMap(
                Function.identity(), String::length, (first, second) -> first, LinkedHashMap::new));
        System.out.println(lengths);	// {Java=4, SQL=3}
        System.out.println(Stream.of("Java", "SQL").collect(Collectors.toUnmodifiableMap(Function.identity(), String::length)).size());	// 2
        System.out.println(new TreeMap<>(source.parallelStream().collect(Collectors.toConcurrentMap(Function.identity(), word -> 1, Integer::sum))));	// {Java=2, SQL=1}
    }

    // toSet does not promise ordering and toCollection lets the caller choose the implementation
    // Stream.toList was added in Java 16 and permits nulls while toUnmodifiableList rejects nulls
    // unmodifiable containers do not make mutable elements deeply immutable
    // use non-null values with toMap and choose a map factory when ordering matters
}
