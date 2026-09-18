import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// stream is a lazy pipeline that processes data without storing it
// intermediate operations build the pipeline and terminal operations trigger evaluation

public class G04_Streams {
    public static void main(String[] args) {
        List<Developer> developers = List.of(
                new Developer("Asha", "Java", 2),
                new Developer("Ravi", "Python", 3),
                new Developer("Neha", "Java", 4)
        );

        List<String> javaDevelopers = developers.stream()
                .filter(developer -> developer.skill().equals("Java"))
                .map(Developer::name)
                .sorted()
                .toList();

        Map<String, Long> countBySkill = developers.stream()
                .collect(Collectors.groupingBy(Developer::skill, Collectors.counting()));

        System.out.println(javaDevelopers);	// [Asha, Neha]
        System.out.println(countBySkill);	// {Java=2, Python=1} in unspecified map order

        // intermediate operations remain lazy until a terminal operation starts traversal
        AtomicInteger processed = new AtomicInteger();
        Stream<Integer> pipeline = Stream.of(1, 2, 3).peek(value -> processed.incrementAndGet());
        System.out.println(processed.get());	// 0
        System.out.println(pipeline.reduce(0, Integer::sum));	// 6
        System.out.println(processed.get());	// 3
    }
}

record Developer(String name, String skill, int experience) {
}
