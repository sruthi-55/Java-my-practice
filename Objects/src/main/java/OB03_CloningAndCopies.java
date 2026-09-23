import java.util.ArrayList;
import java.util.List;

// Object.clone performs a shallow field copy without running constructors
// Cloneable is a marker allowing Object.clone and does not declare a public clone method
// a deep copy duplicates the mutable state that must be independent rather than blindly duplicating everything

public class OB03_CloningAndCopies {
    public static void main(String[] args) throws CloneNotSupportedException {
        // clone creates a different instance while sharing nested mutable references by default
        Notebook original = new Notebook(new ArrayList<>(List.of("Java")));
        Notebook shallow = original.clone();
        Notebook deep = new Notebook(original);
        System.out.println(original == shallow);	// false
        System.out.println(original.getClass() == shallow.getClass());	// true
        System.out.println(original.equals(shallow));	// false
        System.out.println(Notebook.constructions);	// 2
        original.topics.add("SQL");
        System.out.println(shallow.topics);	// [Java, SQL]
        System.out.println(deep.topics);	// [Java]

        // calling Object.clone without Cloneable fails even when a subclass exposes the method
        try {
            new NotCloneable().copy();
        } catch (CloneNotSupportedException exception) {
            System.out.println(exception.getClass().getSimpleName());	// CloneNotSupportedException
        }

        // primitive array clones copy values but multidimensional array clones share nested arrays
        int[] numbers = {1, 2};
        int[] copy = numbers.clone();
        copy[0] = 9;
        System.out.println(numbers[0]);	// 1
        int[][] matrix = {{1}};
        int[][] outerCopy = matrix.clone();
        outerCopy[0][0] = 9;
        System.out.println(matrix[0][0]);	// 9
    }

    static final class Notebook implements Cloneable {
        static int constructions;
        final List<String> topics;
        Notebook(List<String> topics) {
            this.topics = topics;
            constructions++;
        }
        Notebook(Notebook other) { this(new ArrayList<>(other.topics)); }
        @Override
        public Notebook clone() throws CloneNotSupportedException {
            return (Notebook) super.clone();
        }
    }

    static final class NotCloneable {
        Object copy() throws CloneNotSupportedException { return super.clone(); }
    }

    // copy constructors and factories make validation and ownership clearer than exposing clone
    // copying a list is deep enough for immutable String elements but not for mutable element objects
    // graph copies must preserve intentional sharing and account for cycles
}
