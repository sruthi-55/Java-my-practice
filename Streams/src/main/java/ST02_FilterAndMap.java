import java.util.List;
import java.util.stream.Stream;

// filter retains matching elements while map transforms each element into one result
// flatMap flattens mapped streams while mapMulti emits zero or more results through a consumer

public class ST02_FilterAndMap {
    public static void main(String[] args) {
        List<String> words = List.of("Java", "SQL", "Spring");
        System.out.println(words.stream().filter(word -> word.length() > 3).map(String::length).toList());	// [4, 6]

        List<List<Integer>> groups = List.of(List.of(1, 2), List.of(3));
        System.out.println(groups.stream().map(List::size).toList());	// [2, 1]
        System.out.println(groups.stream().flatMap(List::stream).toList());	// [1, 2, 3]

        // emit only even elements without creating a separate stream for each group
        System.out.println(groups.stream().<Integer>mapMulti((group, emit) -> {
            for (int value : group) if (value % 2 == 0) emit.accept(value);
        }).toList());	// [2]

        System.out.println(Stream.of("Java SQL", "Git").flatMap(line -> Stream.of(line.split(" "))).toList());	// [Java, SQL, Git]
        System.out.println(words.stream().mapToInt(String::length).sum());	// 13
        System.out.println(groups.stream().flatMapToInt(group -> group.stream().mapToInt(Integer::intValue)).sum());	// 6
    }

    // flatMap closes each mapped stream after consuming it and treats a null mapped stream as empty
    // mapMulti was added in Java 16 and is useful for small zero-to-many transformations
}
