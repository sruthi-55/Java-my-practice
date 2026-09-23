import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

// a collector defines a supplier, accumulator, combiner, finisher and characteristics
// each parallel partition receives its own mutable container unless concurrent accumulation is explicitly supported

public class ST10_CustomCollectors {
    public static void main(String[] args) {
        // three-argument collect safely builds partition-local mutable lists before combining them
        List<Integer> values = List.of(1, 2, 3);
        ArrayList<Integer> result = values.parallelStream().collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        System.out.println(result);	// [1, 2, 3]

        // this finisher converts the mutable accumulator to an immutable result string
        Collector<String, StringBuilder, String> join = Collector.of(
                StringBuilder::new, StringBuilder::append,
                (left, right) -> left.append(right), StringBuilder::toString);
        System.out.println(List.of("J", "a", "v", "a").parallelStream().collect(join));	// Java
        System.out.println(join.characteristics().isEmpty());	// true

        Collector<Integer, ArrayList<Integer>, ArrayList<Integer>> lists = Collector.of(
                ArrayList::new, ArrayList::add, (left, right) -> { left.addAll(right); return left; });
        System.out.println(lists.characteristics().contains(Collector.Characteristics.IDENTITY_FINISH));	// true
        System.out.println(values.parallelStream().collect(lists));	// [1, 2, 3]

        // concurrent collectors advertise shared accumulation and unordered results explicitly
        var concurrent = Collectors.toConcurrentMap(String::length, word -> 1, Integer::sum);
        System.out.println(concurrent.characteristics().contains(Collector.Characteristics.CONCURRENT));	// true
        System.out.println(concurrent.characteristics().contains(Collector.Characteristics.UNORDERED));	// true
    }

    // IDENTITY_FINISH means accumulator and result are the same without a transforming finisher
    // UNORDERED means result equivalence does not depend on encounter order
    // CONCURRENT requires a container that supports simultaneous accumulation into the same instance
    // ArrayList and StringBuilder must not be declared CONCURRENT and the combiner must be associative
}
