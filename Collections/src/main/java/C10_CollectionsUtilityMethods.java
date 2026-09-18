import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class C10_CollectionsUtilityMethods {
    public static void main(String[] args) {
        List<Integer> numbers = new ArrayList<>(List.of(40, 10, 30, 20, 20));
        Collections.sort(numbers);

        System.out.println("sort: " + numbers);	// sort: [10, 20, 20, 30, 40]
        System.out.println("binarySearch 30: " + Collections.binarySearch(numbers, 30));	// binarySearch 30: 3
        System.out.println("frequency 20: " + Collections.frequency(numbers, 20));	// frequency 20: 2
        System.out.println("min/max: " + Collections.min(numbers) + "/" + Collections.max(numbers));	// min/max: 10/40

        Collections.reverse(numbers);

        System.out.println("reverse: " + numbers);	// reverse: [40, 30, 20, 20, 10]

        Collections.rotate(numbers, 2);

        System.out.println("rotate: " + numbers);	// rotate: [20, 10, 40, 30, 20]

        Collections.swap(numbers, 0, numbers.size() - 1);

        System.out.println("swap: " + numbers);	// swap: [20, 10, 40, 30, 20]

        Collections.fill(numbers, 7);

        System.out.println("fill: " + numbers);	// fill: [7, 7, 7, 7, 7]

        List<Integer> source = List.of(1, 2, 3);
        List<Integer> destination = new ArrayList<>(List.of(0, 0, 0, 0));
        Collections.copy(destination, source);

        System.out.println("copy: " + destination);	// copy: [1, 2, 3, 0]
        System.out.println("disjoint: " + Collections.disjoint(source, List.of(8, 9)));	// disjoint: true

        List<Integer> immutable = Collections.unmodifiableList(source);
        List<Integer> synchronizedList = Collections.synchronizedList(new ArrayList<>(source));
        List<Integer> singleton = Collections.singletonList(42);
        List<Integer> copies = Collections.nCopies(3, 5);

        System.out.println(immutable + " " + synchronizedList + " " + singleton + " " + copies);	// [1, 2, 3] [1, 2, 3] [42] [5, 5, 5]

        List<String> words = new ArrayList<>(List.of("bbb", "a", "cc"));
        words.sort(Comparator.comparingInt(String::length).thenComparing(Comparator.naturalOrder()));

        System.out.println("comparator chain: " + words);	// comparator chain: [a, cc, bbb]
    }

    // binarySearch requires the list to be sorted using an order compatible with the search
    // unmodifiable wrappers reject mutation while synchronized wrappers serialize individual method calls
}
