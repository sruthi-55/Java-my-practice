import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;
import java.util.TreeSet;

// equals must be reflexive, symmetric, transitive, consistent and false for null
// equal objects require equal hashes and hash stability depends on unchanged equality-relevant state
// override equals(Object) and hashCode together rather than overloading equals with a narrower parameter

public class OB02_EqualityAndHashing {
    public static void main(String[] args) {
        // records generate value equality and hashes from their components
        Key first = new Key(1);
        Key second = new Key(1);
        Key third = new Key(1);
        System.out.println(first.equals(first));	// true
        System.out.println(first.equals(second) && second.equals(first));	// true
        System.out.println(first.equals(second) && second.equals(third) && first.equals(third));	// true
        System.out.println(first.equals(second) == first.equals(second));	// true
        System.out.println(first.equals(null));	// false
        System.out.println(first.hashCode() == second.hashCode());	// true
        System.out.println(new HashSet<>(List.of(first, second)).size());	// 1

        // identity maps deliberately keep logically equal but distinct keys separate
        Map<Key, String> identities = new IdentityHashMap<>();
        identities.put(first, "first");
        identities.put(second, "second");
        System.out.println(identities.size());	// 2

        // changing a stored key's hash can make even same-reference lookup fail
        MutableKey mutable = new MutableKey(1);
        Map<MutableKey, String> values = new HashMap<>();
        values.put(mutable, "Java");
        mutable.id = 2;
        System.out.println(values.containsKey(mutable));	// false
        System.out.println(values.size());	// 1
        mutable.id = 1;
        System.out.println(values.remove(mutable));	// Java

        // equals with a subtype parameter overloads instead of overriding Object.equals
        Overloaded left = new Overloaded(1);
        Overloaded right = new Overloaded(1);
        System.out.println(left.equals(right));	// true
        System.out.println(((Object) left).equals(right));	// false

        // mixed superclass and subclass equality policies can break symmetry
        Point point = new Point(1);
        Point colored = new ColoredPoint(1, "red");
        System.out.println(point.equals(colored));	// true
        System.out.println(colored.equals(point));	// false

        // BigDecimal equality includes scale while natural comparison uses numeric value
        BigDecimal one = new BigDecimal("1.0");
        BigDecimal sameValue = new BigDecimal("1.00");
        System.out.println(one.equals(sameValue));	// false
        System.out.println(one.compareTo(sameValue));	// 0
        System.out.println(new HashSet<>(List.of(one, sameValue)).size());	// 2
        System.out.println(new TreeSet<>(List.of(one, sameValue)).size());	// 1
    }

    record Key(int id) {}

    static final class MutableKey {
        int id;
        MutableKey(int id) { this.id = id; }
        @Override
        public boolean equals(Object other) {
            return other instanceof MutableKey key && id == key.id;
        }
        @Override
        public int hashCode() { return id; }
    }

    // deliberately incorrect overload illustrates why @Override is useful on equality methods
    static final class Overloaded {
        final int id;
        Overloaded(int id) { this.id = id; }
        public boolean equals(Overloaded other) { return other != null && id == other.id; }
    }

    // deliberately incompatible equality policies illustrate an inheritance contract violation
    static class Point {
        final int x;
        Point(int x) { this.x = x; }
        @Override
        public boolean equals(Object other) { return other instanceof Point point && x == point.x; }
        @Override
        public int hashCode() { return x; }
    }

    static final class ColoredPoint extends Point {
        final String color;
        ColoredPoint(int x, String color) { super(x); this.color = color; }
        @Override
        public boolean equals(Object other) {
            return other instanceof ColoredPoint point && super.equals(point) && color.equals(point.color);
        }
    }

    // prefer final value classes or composition when additional subtype state would change equality
    // getClass-based equality rejects other runtime classes while instanceof permits compatible subtypes
    // a constant hash is legal but harms hash-table performance and cannot replace a useful hash function
}
