import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Stream;

// streams can originate from collections, arrays, builders, generators and resource-backed APIs
// a Map is streamed through its keySet, values or entrySet views

public class ST01_StreamSources {
    public static void main(String[] args) {
        System.out.println(List.of(1, 2).stream().toList());	// [1, 2]
        System.out.println(Arrays.stream(new String[]{"Java", "SQL"}).toList());	// [Java, SQL]
        System.out.println(Stream.of("A", "B").toList());	// [A, B]
        System.out.println(Stream.empty().count());	// 0
        System.out.println(Stream.ofNullable(null).count());	// 0
        System.out.println(Stream.ofNullable("Java").toList());	// [Java]
        System.out.println(Stream.<String>builder().add("A").add("B").build().toList());	// [A, B]
        System.out.println(Stream.concat(Stream.of(1, 2), Stream.of(3)).toList());	// [1, 2, 3]

        // Stream.of on one primitive array creates one array element rather than an IntStream
        int[] values = {1, 2, 3};
        System.out.println(Stream.of(values).count());	// 1
        System.out.println(Arrays.stream(values).sum());	// 6

        Map<String, Integer> scores = Map.of("Java", 2, "SQL", 1);
        System.out.println(scores.entrySet().stream().map(entry -> entry.getKey() + "=" + entry.getValue()).sorted().toList());	// [Java=2, SQL=1]
        System.out.println(Pattern.compile(",").splitAsStream("Java,SQL").toList());	// [Java, SQL]
        System.out.println("Java\nSQL".lines().toList());	// [Java, SQL]
    }

    // ofNullable is available since Java 9 and String.lines since Java 11
    // collection-backed streams usually need no cleanup while Files.lines needs explicit closure
}
