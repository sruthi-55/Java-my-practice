import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

// interview pipelines combine transformation, grouping and selection with explicit duplicate and ordering rules
// choose readable operations and test empty input, duplicates and ties instead of forcing every problem into a stream

public class ST14_InterviewProblems {
    public static void main(String[] args) {
        List<Integer> numbers = List.of(5, 3, 5, 2, 4, 3);

        // second highest distinct value handles duplicates before selecting a position
        int second = numbers.stream().distinct().sorted(Comparator.reverseOrder()).skip(1).findFirst().orElseThrow();
        System.out.println(second);	// 4
        check(second == 4, "second highest");

        // frequency maps support duplicate detection without mutable filter predicates
        Map<Integer, Long> counts = numbers.stream().collect(Collectors.groupingBy(Function.identity(), TreeMap::new, Collectors.counting()));
        System.out.println(counts);	// {2=1, 3=2, 4=1, 5=2}
        List<Integer> duplicates = counts.entrySet().stream().filter(entry -> entry.getValue() > 1).map(Map.Entry::getKey).toList();
        System.out.println(duplicates);	// [3, 5]
        check(duplicates.equals(List.of(3, 5)), "duplicates");

        // encounter-order grouping finds the first non-repeated Unicode code point
        Map<Integer, Long> characters = "swiss".codePoints().boxed().collect(Collectors.groupingBy(
                Function.identity(), LinkedHashMap::new, Collectors.counting()));
        String unique = characters.entrySet().stream().filter(entry -> entry.getValue() == 1)
                .map(entry -> new String(Character.toChars(entry.getKey()))).findFirst().orElse("none");
        System.out.println(unique);	// w
        check(unique.equals("w"), "first unique character");

        // frequency descending and key ascending produce deterministic top-k results when counts tie
        List<Integer> top = counts.entrySet().stream()
                .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey()))
                .limit(2).map(Map.Entry::getKey).toList();
        System.out.println(top);	// [3, 5]
        check(top.equals(List.of(3, 5)), "top two");

        List<String> flattened = List.of(List.of("Java", "SQL"), List.of("Java", "Git")).stream()
                .flatMap(List::stream).distinct().sorted().toList();
        System.out.println(flattened);	// [Git, Java, SQL]
        check(flattened.equals(List.of("Git", "Java", "SQL")), "flattened skills");
        check(List.<Integer>of().stream().distinct().skip(1).findFirst().isEmpty(), "empty input");
        check(List.of(5, 5).stream().distinct().skip(1).findFirst().isEmpty(), "no second distinct value");
        System.out.println("checks passed");	// checks passed
    }

    static void check(boolean condition, String scenario) {
        if (!condition) throw new AssertionError(scenario);
    }

    // sorting all n values costs O(n log n); a bounded heap can be preferable for large top-k problems
    // team aggregates and highest salary per team are demonstrated in ST09_GroupingAndPartitioning
}
