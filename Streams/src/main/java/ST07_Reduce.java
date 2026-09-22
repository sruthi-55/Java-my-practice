import java.util.List;
import java.util.stream.Stream;

// reduce combines elements using an associative operation and optionally a neutral identity
// the three-argument form needs a combiner compatible with the identity and accumulator

public class ST07_Reduce {
    public static void main(String[] args) {
        List<Integer> values = List.of(1, 2, 3);
        System.out.println(values.stream().reduce(0, Integer::sum));	// 6
        System.out.println(values.stream().reduce(Integer::sum).orElseThrow());	// 6
        System.out.println(Stream.<Integer>empty().reduce(Integer::sum).isEmpty());	// true
        System.out.println(Stream.<Integer>empty().reduce(0, Integer::sum));	// 0
        System.out.println(Stream.of("Java", "SQL").parallel().reduce(0, (length, word) -> length + word.length(), Integer::sum));	// 7

        // a non-neutral identity is injected into each partition and breaks a parallel reduction
        int whole = values.stream().reduce(10, Integer::sum);
        int split = Stream.of(1).reduce(10, Integer::sum) + Stream.of(2, 3).reduce(10, Integer::sum);
        System.out.println(whole + " " + split);	// 16 26

        // subtraction is not associative so regrouping changes the answer
        System.out.println((10 - 3) - 2);	// 5
        System.out.println(10 - (3 - 2));	// 9
    }

    // do not mutate a shared ArrayList or StringBuilder identity in reduce; use collect for mutable results
    // addition uses zero, multiplication uses one and concatenation uses an empty string as identity
}
