import java.util.List;
import java.util.ArrayList;
import java.util.function.Supplier;
import java.util.stream.Stream;

// Stream is a sequence of elements supporting sequential or parallel aggregate operations
// a pipeline consists of a source, lazy intermediate operations and a terminal operation
// collections store elements while streams describe computations and do not store their source data

public class ST00_StreamIntroduction {
    public static void main(String[] args) {
        List<String> words = List.of("Java", "SQL", "Spring");

        // nothing in filter runs until the terminal operation requests elements
        Stream<String> pipeline = words.stream().filter(word -> {
            System.out.println("checking " + word);	// checking Java, checking SQL, checking Spring
            return word.length() > 3;
        });
        System.out.println("pipeline created");	// pipeline created
        System.out.println(pipeline.map(String::toUpperCase).toList());	// [JAVA, SPRING]
        System.out.println(words);	// [Java, SQL, Spring]

        // a consumed stream cannot be reused even when its source collection still exists
        try {
            pipeline.count();
        } catch (IllegalStateException exception) {
            System.out.println(exception.getClass().getSimpleName());	// IllegalStateException
        }

        // a supplier creates a fresh pipeline for each traversal
        Supplier<Stream<String>> fresh = words::stream;
        System.out.println(fresh.get().count());	// 3
        System.out.println(fresh.get().findFirst().orElse("none"));	// Java

        // ArrayList streams are late-binding so changes before traversal are visible to the pipeline
        List<String> mutable = new ArrayList<>(List.of("Java"));
        Stream<String> lateBinding = mutable.stream();
        mutable.add("SQL");
        System.out.println(lateBinding.toList());	// [Java, SQL]
    }

    // internal iteration lets the stream control traversal while an iterator or loop uses external iteration
    // lambdas should be stateless and non-interfering rather than mutating the source during traversal
    // streams were introduced in Java 8 and this module targets Java 21 without preview APIs
}
