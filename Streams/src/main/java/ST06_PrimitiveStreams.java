import java.util.IntSummaryStatistics;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

// IntStream, LongStream and DoubleStream specialize primitive processing and avoid per-element boxing
// range excludes its upper bound while rangeClosed includes it

public class ST06_PrimitiveStreams {
    public static void main(String[] args) {
        System.out.println(IntStream.range(1, 4).boxed().toList());	// [1, 2, 3]
        System.out.println(IntStream.rangeClosed(1, 4).sum());	// 10
        System.out.println(LongStream.of(10L, 20L).sum());	// 30
        System.out.println(DoubleStream.of(1.5, 2.5).average().orElseThrow());	// 2.0
        System.out.println(Stream.of("Java", "SQL").mapToLong(String::length).sum());	// 7
        System.out.println(Stream.of(1, 2).mapToDouble(value -> value / 2.0).sum());	// 1.5
        System.out.println(IntStream.of(1, 2).asLongStream().sum());	// 3
        System.out.println(IntStream.of(1, 2).asDoubleStream().sum());	// 3.0
        System.out.println(IntStream.rangeClosed(1, 2).mapToObj(value -> "id-" + value).toList());	// [id-1, id-2]

        IntSummaryStatistics stats = IntStream.of(10, 20, 30).summaryStatistics();
        System.out.println(stats.getCount() + " " + stats.getSum() + " " + stats.getMin() + " " + stats.getMax() + " " + stats.getAverage());	// 3 60 10 30 20.0
        System.out.println(IntStream.empty().min().isEmpty());	// true
        System.out.println(IntStream.empty().average().orElse(0));	// 0.0
        System.out.println(IntStream.empty().sum());	// 0

        // chars emits UTF-16 code units while codePoints respects supplementary Unicode characters
        System.out.println("A😀".chars().count());	// 3
        System.out.println("A😀".codePoints().count());	// 2
    }

    // min and max return OptionalInt, OptionalLong or OptionalDouble and average returns OptionalDouble
    // int and long sums can overflow and floating-point reductions are subject to rounding
}
