import java.util.List;
import java.util.stream.Stream;

// limit and skip select by position while takeWhile and dropWhile operate on a matching prefix of ordered streams
// infinite streams require an operation that can make traversal terminate

public class ST04_SlicingAndInfiniteStreams {
    public static void main(String[] args) {
        List<Integer> numbers = List.of(1, 2, 5, 3);
        System.out.println(numbers.stream().skip(1).limit(2).toList());	// [2, 5]
        System.out.println(numbers.stream().takeWhile(value -> value < 4).toList());	// [1, 2]
        System.out.println(numbers.stream().dropWhile(value -> value < 4).toList());	// [5, 3]
        System.out.println(numbers.stream().filter(value -> value < 4).toList());	// [1, 2, 3]

        System.out.println(Stream.iterate(1, value -> value + 1).limit(4).toList());	// [1, 2, 3, 4]
        System.out.println(Stream.iterate(1, value -> value < 5, value -> value + 1).toList());	// [1, 2, 3, 4]
        System.out.println(Stream.generate(() -> "Java").limit(2).toList());	// [Java, Java]
        System.out.println(Stream.iterate(1, value -> value + 1).anyMatch(value -> value == 3));	// true
    }

    // sorted before limit on an infinite stream cannot finish because sorting needs the entire input
    // filter before limit can still run forever if too few elements satisfy the filter
    // negative skip or limit arguments throw IllegalArgumentException
    // takeWhile, dropWhile and bounded iterate were added in Java 9
    // unordered takeWhile and dropWhile do not guarantee an encounter-order prefix
}
