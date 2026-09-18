import java.util.HashMap;
import java.util.Map;

public class C02_HashMap {
    public static void main(String[] args) {
        Map<EmployeeId, String> employees = new HashMap<>();
        employees.put(new EmployeeId(101), "Sruthi");
        employees.put(new EmployeeId(102), "Asha");

        System.out.println(employees.get(new EmployeeId(101)));	// Sruthi
        employees.merge(new EmployeeId(101), "Java", (name, skill) -> name + " - " + skill);
        // if the key already exists, combine the old val and new val using this fn

        System.out.println(employees);	// both employee mappings in unspecified order
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
