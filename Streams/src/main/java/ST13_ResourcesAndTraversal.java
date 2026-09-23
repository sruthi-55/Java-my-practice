import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

// resource-backed streams implement AutoCloseable and need try-with-resources for deterministic cleanup
// Spliterator supports traversal, splitting and source characteristics used by stream implementations

public class ST13_ResourcesAndTraversal {
    public static void main(String[] args) throws IOException {
        Path file = Files.createTempFile("stream-practice-", ".txt");
        try {
            Files.write(file, List.of("Java", "SQL"), StandardCharsets.UTF_8);
            try (Stream<String> lines = Files.lines(file, StandardCharsets.UTF_8).onClose(() -> System.out.println("closed"))) {	// closed
                System.out.println(lines.toList());	// [Java, SQL]
            }
        } finally {
            Files.delete(file);
        }

        // terminal completion does not automatically invoke a stream's close handlers
        try (Stream<Integer> values = Stream.of(1).onClose(() -> System.out.println("handler one"))	// handler one
                .onClose(() -> System.out.println("handler two"))) {	// handler two
            System.out.println(values.count());	// 1
        }

        Iterator<Integer> iterator = Stream.of(1, 2).iterator();
        iterator.forEachRemaining(System.out::println);	// 1, then 2

        Spliterator<Integer> remainder = new ArrayList<>(List.of(1, 2, 3, 4)).spliterator();
        System.out.println(remainder.hasCharacteristics(Spliterator.ORDERED | Spliterator.SIZED));	// true
        System.out.println(remainder.estimateSize());	// 4
        Spliterator<Integer> prefix = remainder.trySplit();
        List<Integer> traversed = new ArrayList<>();
        if (prefix != null) prefix.forEachRemaining(traversed::add);
        remainder.tryAdvance(traversed::add);
        remainder.forEachRemaining(traversed::add);
        System.out.println(traversed);	// [1, 2, 3, 4]
        System.out.println(StreamSupport.stream(List.of(1, 2).spliterator(), false).toList());	// [1, 2]

        // close handlers all run and later failures are suppressed on the first handler failure
        try (Stream<Integer> failing = Stream.of(1)
                .onClose(() -> { throw new IllegalStateException("first close"); })
                .onClose(() -> { throw new IllegalArgumentException("second close"); })) {
            System.out.println(failing.count());	// 1
        } catch (IllegalStateException exception) {
            System.out.println(exception.getMessage());	// first close
            System.out.println(exception.getSuppressed()[0].getMessage());	// second close
        }
    }

    // onClose handlers run in registration order and the first failure retains later failures as suppressed
    // file streams can throw IOException when opened and UncheckedIOException during lazy traversal
    // Files.list and Files.walk also return streams that require closure
    // SIZED indicates a known size and SUBSIZED indicates sized splits; IMMUTABLE and CONCURRENT describe the source
    // iterator and spliterator consume stream ownership and prevent a second terminal traversal
}
