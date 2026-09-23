import java.util.HashMap;
import java.util.Map;

// HashMap uses buckets selected by a spread hash and equals distinguishes keys within a bucket
// equal keys replace one mapping while hash collisions can retain multiple unequal keys
// capacity is bucket storage and load factor controls the resize threshold rather than the maximum map size
// get and put are expected O(1) with well-distributed hashes while resizing takes O(n)
// modern OpenJDK can treeify crowded buckets but thresholds are implementation details rather than Map guarantees
// HashMap is not thread-safe and mutable keys can break lookup as shown in Objects/OB02_EqualityAndHashing

public class C02_HashMap {
    public static void main(String[] args) {
        Map<EmployeeId, String> employees = new HashMap<>();
        employees.put(new EmployeeId(101), "Sruthi");
        employees.put(new EmployeeId(102), "Asha");

        System.out.println(employees.get(new EmployeeId(101)));	// Sruthi
        employees.merge(new EmployeeId(101), "Java", (name, skill) -> name + " - " + skill);
        // if the key already exists, combine the old val and new val using this fn

        System.out.println(employees);	// both employee mappings in unspecified order

        // replacing an equal key changes its value without increasing the number of mappings
        System.out.println(employees.put(new EmployeeId(102), "Neha"));	// Asha
        System.out.println(employees.size());	// 2
    }
}

// HashMap uses hashCode to find a bucket and equals to identify the key within it
// record automatically implements equals()
// Java automatically generates value-based equal()
record EmployeeId(int value) {
    // special kind of class designed for holding immutable data
    // Java will generate constructors, getters, equals(), hashCode(), toString() ...etc
    // no setters because record components are final
}
