import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

// explicit checks enforce contracts without requiring the -ea JVM option

public class OB08_ObjectChecks {
    public static void main(String[] args) throws CloneNotSupportedException {
        // equal record values obey identity-independent equality and hash collection contracts
        var first = new OB02_EqualityAndHashing.Key(1);
        var second = new OB02_EqualityAndHashing.Key(1);
        check(false, first == second);
        check(true, first.equals(second));
        check(true, second.equals(first));
        check(false, first.equals(null));
        check(false, first.equals("1"));
        check(first.hashCode(), second.hashCode());
        check(1, new HashSet<>(List.of(first, second)).size());

        // cloning duplicates array storage while deep equality examines nested contents
        int[] original = {1};
        int[] copy = original.clone();
        check(false, original == copy);
        check(true, Objects.deepEquals(original, copy));
        copy[0] = 2;
        check(1, original[0]);
        check(true, Arrays.deepEquals(new Object[]{new int[]{1}}, new Object[]{original}));

        // clone without Cloneable fails with the documented checked exception
        try {
            new OB03_CloningAndCopies.NotCloneable().copy();
            throw new AssertionError("unsupported clone succeeded");
        } catch (CloneNotSupportedException expected) {
            check(CloneNotSupportedException.class, expected.getClass());
        }

        // zero-sized end ranges are valid but an element index at the length is invalid
        check(3, Objects.checkFromIndexSize(3, 0, 3));
        try {
            Objects.checkIndex(3, 3);
            throw new AssertionError("invalid index accepted");
        } catch (IndexOutOfBoundsException expected) {
            check(true, expected instanceof IndexOutOfBoundsException);
        }
        System.out.println("Object checks passed");	// Object checks passed
    }

    private static void check(Object expected, Object actual) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError("expected " + expected + " but got " + actual);
        }
    }
}
