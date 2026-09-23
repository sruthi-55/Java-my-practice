import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

// sorted is stateful and typically buffers input while distinct tracks equality to remove duplicates
// encounter order comes from the source and sorted defines a new order using a comparator

public class ST03_SortingAndDistinct {
    public static void main(String[] args) {
        List<Integer> numbers = List.of(3, 1, 3, 2);
        System.out.println(numbers.stream().distinct().toList());	// [3, 1, 2]
        System.out.println(numbers.stream().sorted().toList());	// [1, 2, 3, 3]
        System.out.println(numbers.stream().distinct().sorted(Comparator.reverseOrder()).toList());	// [3, 2, 1]

        // comparator chains resolve ties while ordered-stream sorting is stable for equal comparisons
        System.out.println(Stream.of("bb", "aa", "c")
                .sorted(Comparator.comparingInt(String::length).thenComparing(Comparator.naturalOrder())).toList());	// [c, aa, bb]

        // record equality includes every component so equal records collapse under distinct
        System.out.println(Stream.of(new Skill("Java"), new Skill("Java"), new Skill("SQL")).distinct().toList());	// [Skill[name=Java], Skill[name=SQL]]

        // stable sorting retains ties in encounter order and distinct still uses equals rather than the comparator
        System.out.println(Stream.of("bb", "aa", "bb").sorted(Comparator.comparingInt(String::length)).distinct().toList());	// [bb, aa]
    }

    record Skill(String name) { }

    // custom classes need consistent equals and hashCode for meaningful value-based distinct behavior
    // HashSet and HashMap do not promise encounter order and unordered does not mean shuffled
    // sorting before distinct does not redefine equality to match the comparator
}
