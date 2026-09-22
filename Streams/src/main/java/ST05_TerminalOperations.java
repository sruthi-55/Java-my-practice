import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

// terminal operations consume the stream and return a result or perform an action
// matching and finding are short-circuiting operations that may avoid traversing every element

public class ST05_TerminalOperations {
    public static void main(String[] args) {
        List<Integer> values = List.of(3, 1, 2);
        System.out.println(values.stream().count());	// 3
        System.out.println(values.stream().min(Comparator.naturalOrder()).orElseThrow());	// 1
        System.out.println(values.stream().max(Comparator.naturalOrder()).orElseThrow());	// 3
        System.out.println(values.stream().findFirst().orElseThrow());	// 3
        System.out.println(values.contains(values.parallelStream().findAny().orElseThrow()));	// true
        System.out.println(values.stream().anyMatch(value -> value == 1));	// true
        System.out.println(values.stream().allMatch(value -> value > 0));	// true
        System.out.println(values.stream().noneMatch(value -> value < 0));	// true
        System.out.println(Arrays.toString(values.stream().toArray(Integer[]::new)));	// [3, 1, 2]
        values.stream().forEach(System.out::println);	// 3, then 1, then 2

        // empty streams have no match but satisfy allMatch and noneMatch without evaluating a predicate
        System.out.println(Stream.<Integer>empty().anyMatch(value -> true));	// false
        System.out.println(Stream.<Integer>empty().allMatch(value -> false));	// true
        System.out.println(Stream.<Integer>empty().noneMatch(value -> true));	// true
        System.out.println(Stream.empty().findFirst().isEmpty());	// true
    }

    // findFirst preserves encounter order when one exists while findAny deliberately permits any element
    // there is no general break or continue in forEach; express stopping with a short-circuit operation
}
