import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;

public class C08_SetMethods {
    public static void main(String[] args) {
        Set<Integer> numbers = new HashSet<>();
        System.out.println("add new: " + numbers.add(10));	// add new: true
        System.out.println("add duplicate: " + numbers.add(10));	// add duplicate: false
        numbers.addAll(Set.of(20, 30, 40));

        System.out.println("contains: " + numbers.contains(20));	// contains: true
        System.out.println("containsAll: " + numbers.containsAll(Set.of(10, 20)));	// containsAll: true
        numbers.remove(40);
        numbers.removeIf(number -> number > 25);

        System.out.println("HashSet: " + numbers);	// HashSet: [10, 20] in unspecified order

        Set<Integer> union = new HashSet<>(numbers);
        union.addAll(Set.of(20, 50));
        Set<Integer> intersection = new HashSet<>(numbers);
        intersection.retainAll(Set.of(20, 50));
        Set<Integer> difference = new HashSet<>(union);
        difference.removeAll(numbers);

        System.out.println("union: " + union);	// union containing 10, 20 and 50 in unspecified order
        System.out.println("intersection: " + intersection);	// intersection: [20]
        System.out.println("difference: " + difference);	// difference: [50]

        Set<String> insertionOrder = new LinkedHashSet<>(List.of("Java", "SQL", "Git"));

        System.out.println("LinkedHashSet: " + insertionOrder);	// LinkedHashSet: [Java, SQL, Git]

        NavigableSet<Integer> sorted = new TreeSet<>(Set.of(10, 20, 30, 40, 50));

        System.out.println("first/last: " + sorted.first() + "/" + sorted.last());	// first/last: 10/50
        System.out.println("lower/floor: " + sorted.lower(30) + "/" + sorted.floor(30));	// lower/floor: 20/30
        System.out.println("ceiling/higher: " + sorted.ceiling(30) + "/" + sorted.higher(30));	// ceiling/higher: 30/40
        System.out.println("headSet: " + sorted.headSet(30, true));	// headSet: [10, 20, 30]
        System.out.println("tailSet: " + sorted.tailSet(30, false));	// tailSet: [40, 50]
        System.out.println("subSet: " + sorted.subSet(20, true, 50, false));	// subSet: [20, 30, 40]
        System.out.println("descendingSet: " + sorted.descendingSet());	// descendingSet: [50, 40, 30, 20, 10]
        System.out.println("pollFirst/pollLast: " + sorted.pollFirst() + "/" + sorted.pollLast());	// pollFirst/pollLast: 10/50
    }

    // Set.add returns false for an equal duplicate because sets contain no duplicate elements
    // addAll, retainAll and removeAll implement union, intersection and difference when used with copied sets
}
