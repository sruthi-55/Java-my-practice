import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

// groupingBy classifies elements into keys while partitioningBy creates true and false groups
// downstream collectors transform or aggregate the elements within each group

public class ST09_GroupingAndPartitioning {
    public static void main(String[] args) {
        List<Employee> employees = List.of(
                new Employee("Asha", "Dev", 100, List.of("Java", "SQL")),
                new Employee("Ravi", "QA", 80, List.of("Testing")),
                new Employee("Neha", "Dev", 120, List.of("Java", "Git")));

        var counts = employees.stream().collect(Collectors.groupingBy(Employee::team, TreeMap::new, Collectors.counting()));
        System.out.println(counts);	// {Dev=2, QA=1}
        var totals = employees.stream().collect(Collectors.groupingBy(Employee::team, TreeMap::new, Collectors.summingInt(Employee::salary)));
        System.out.println(totals);	// {Dev=220, QA=80}
        var averages = employees.stream().collect(Collectors.groupingBy(Employee::team, TreeMap::new, Collectors.averagingInt(Employee::salary)));
        System.out.println(averages);	// {Dev=110.0, QA=80.0}
        var names = employees.stream().collect(Collectors.groupingBy(Employee::team, TreeMap::new,
                Collectors.mapping(Employee::name, Collectors.toList())));
        System.out.println(names);	// {Dev=[Asha, Neha], QA=[Ravi]}

        // downstream filtering retains a group with an empty result unlike filtering before grouping
        var filtered = employees.stream().collect(Collectors.groupingBy(Employee::team, TreeMap::new,
                Collectors.filtering(employee -> employee.salary() > 100, Collectors.counting())));
        System.out.println(filtered);	// {Dev=1, QA=0}

        var skills = employees.stream().collect(Collectors.groupingBy(Employee::team, TreeMap::new,
                Collectors.flatMapping(employee -> employee.skills().stream(), Collectors.toCollection(TreeSet::new))));
        System.out.println(skills);	// {Dev=[Git, Java, SQL], QA=[Testing]}

        // collectingAndThen transforms the finished group result and unwraps the nonempty group's maximum
        var highest = employees.stream().collect(Collectors.groupingBy(Employee::team, TreeMap::new,
                Collectors.collectingAndThen(Collectors.maxBy(Comparator.comparingInt(Employee::salary)),
                        best -> best.orElseThrow().name())));
        System.out.println(highest);	// {Dev=Neha, QA=Ravi}

        System.out.println(new TreeMap<>(employees.stream().collect(Collectors.partitioningBy(employee -> employee.salary() >= 100,
                Collectors.mapping(Employee::name, Collectors.toList())))));	// {false=[Ravi], true=[Asha, Neha]}
        var nested = employees.stream().collect(Collectors.groupingBy(Employee::team, TreeMap::new,
                Collectors.groupingBy(employee -> employee.salary() >= 100, TreeMap::new, Collectors.counting())));
        System.out.println(nested);	// {Dev={true=2}, QA={false=1}}

        // summarizing and teeing calculate multiple aggregates in one traversal
        System.out.println(employees.stream().collect(Collectors.summarizingInt(Employee::salary)).getSum());	// 300
        double average = employees.stream().collect(Collectors.teeing(Collectors.summingInt(Employee::salary),
                Collectors.counting(), (sum, count) -> sum / (double) count));
        System.out.println(average);	// 100.0
        System.out.println(employees.stream().collect(Collectors.reducing(0, Employee::salary, Integer::sum)));	// 300
        System.out.println(employees.stream().collect(Collectors.minBy(Comparator.comparingInt(Employee::salary))).orElseThrow().name());	// Ravi
        System.out.println(new TreeMap<>(employees.parallelStream().collect(Collectors.groupingByConcurrent(Employee::team, Collectors.counting()))));	// {Dev=2, QA=1}
    }

    record Employee(String name, String team, int salary, List<String> skills) { }

    // groupingBy rejects null classifier results and groupingByConcurrent does not preserve encounter order
    // partitioningBy includes both Boolean keys even when one partition is empty
    // filtering and flatMapping collectors arrived in Java 9 and teeing in Java 12
}
