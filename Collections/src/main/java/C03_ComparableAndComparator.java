import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Arrays;
import java.util.TreeSet;

// Comparable interface defines one natural ordering inside the class through compareTo()
// Comparator defines external custom orderings through compare() without modifying the compared class
// compare and compareTo return a negative value, zero or a positive value for less than, equal to or greater than
// Comparator is a functional interface, so it can be implemented with a lambda or method reference
// comparison must be sign-symmetric and transitive with consistent results for ties against a third value
// sorted sets and maps treat comparison zero as the same element or key even when equals disagrees
// subtraction can overflow so compare numeric keys with Integer.compare or comparingInt

public class C03_ComparableAndComparator {
    public static void main(String[] args) {
        List<Candidate> candidates = new ArrayList<>(List.of(
                new Candidate("Ravi", 3),
                new Candidate("Asha", 2),
                new Candidate("Neha", 2),
                new Candidate("Asha", 4)
        ));

        Collections.sort(candidates);
        System.out.println(candidates);	// experience ascending: Asha-2, Neha-2, Ravi-3, Asha-4

        Comparator<Candidate> byName = Comparator.comparing(Candidate::name);
        candidates.sort(byName);
        System.out.println(candidates);	// name ascending: Asha-2, Asha-4, Neha-2, Ravi-3

        Comparator<Candidate> byNameThenExperienceDescending = Comparator
                .comparing(Candidate::name)
                .thenComparing(Comparator.comparingInt(Candidate::experience).reversed());
        candidates.sort(byNameThenExperienceDescending);
        System.out.println(candidates);	// name ascending and experience descending: Asha-4, Asha-2, Neha-2, Ravi-3

        Comparator<Candidate> lambdaComparator = (first, second) -> Integer.compare(first.experience(), second.experience());
        candidates.sort(lambdaComparator.thenComparing(Candidate::name));
        System.out.println(candidates);	// experience ascending and name ascending: Asha-2, Neha-2, Ravi-3, Asha-4

        candidates.sort(Comparator.naturalOrder());
        System.out.println(candidates);	// natural order: Asha-2, Neha-2, Ravi-3, Asha-4

        candidates.sort(Comparator.reverseOrder());
        System.out.println(candidates);	// reverse natural order: Asha-4, Ravi-3, Asha-2, Neha-2

        System.out.println(Integer.signum(byName.compare(candidates.get(0), candidates.get(1))));	// -1
        System.out.println(Integer.signum(byName.compare(candidates.get(2), candidates.get(3))));	// -1
        System.out.println(Integer.signum(byName.compare(candidates.get(2), candidates.get(2))));	// 0
        System.out.println(byName.equals(byName));	// true

        // nullable values need an explicit null ordering before comparing their contents
        List<String> names = new ArrayList<>(Arrays.asList("Java", null, "Git"));
        names.sort(Comparator.nullsLast(Comparator.naturalOrder()));
        System.out.println(names);	// [Git, Java, null]

        // this natural order compares only experience and therefore collapses unequal candidates with ties
        TreeSet<Candidate> sameExperience = new TreeSet<>();
        sameExperience.add(new Candidate("Asha", 2));
        sameExperience.add(new Candidate("Neha", 2));
        System.out.println(sameExperience.size());	// 1
        System.out.println(Integer.MAX_VALUE - (-1) < 0);	// true
        System.out.println(Integer.compare(Integer.MAX_VALUE, -1));	// 1
    }

    // natural order is used by Collections.sort(list), List.sort(null), TreeSet and TreeMap
    // custom order is passed explicitly to List.sort, Collections.sort, TreeSet, TreeMap or PriorityQueue
    // comparing extracts a sort key and thenComparing adds a tie-breaker
    // reversed flips the current comparator while reverseOrder uses the reverse of natural ordering
    // Comparator.equals checks whether comparator objects are equal but most comparators retain Object equality
}

record Candidate(String name, int experience) implements Comparable<Candidate> {
    @Override
    public int compareTo(Candidate other) {
        return Integer.compare(experience, other.experience);
    }

    @Override
    public String toString() {
        return name + "-" + experience;
    }
}


// Comparator.compare compares elements while Comparator.equals compares comparator objects
// static factories include comparing, comparingInt, naturalOrder, reverseOrder, nullsFirst and nullsLast
// default methods include thenComparing, thenComparingInt and reversed

// Comparable.compareTo defines the implementing type's natural order
