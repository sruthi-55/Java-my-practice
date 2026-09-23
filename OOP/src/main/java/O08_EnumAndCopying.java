import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

// enum defines a fixed set of named instances and can contain fields, constructors and methods
// shallow copy shares nested object references while deep copy duplicates mutable nested state

public class O08_EnumAndCopying {
    public static void main(String[] args) {
        Task original = new Task("Prepare Java", new ArrayList<>(List.of("OOP")), Priority.HIGH);
        Task copy = new Task(original);
        original.topics().add("Collections");

        System.out.println(original);	// Task[title=Prepare Java, topics=[OOP, Collections], priority=HIGH]
        System.out.println(copy);	// Task[title=Prepare Java, topics=[OOP], priority=HIGH]
        System.out.println(Priority.HIGH.label());	// Do now
        System.out.println(Arrays.toString(Priority.values()));	// [LOW, MEDIUM, HIGH]
        System.out.println(Priority.valueOf("HIGH") == Priority.HIGH);	// true
        System.out.println(Priority.HIGH.name() + " " + Priority.HIGH.ordinal());	// HIGH 2
        System.out.println(switch (Priority.HIGH) { case HIGH -> "urgent"; case LOW, MEDIUM -> "normal"; });	// urgent

        CloneableTopics shallowOriginal = new CloneableTopics(new ArrayList<>(List.of("Java")));
        CloneableTopics shallowCopy = shallowOriginal.clone();
        shallowOriginal.values().add("SQL");
        System.out.println(shallowCopy.values());	// [Java, SQL]
    }
}

// enum constructors are not public and enum instances are compared safely with ==
// do not persist ordinal as a business identifier because declaration reordering changes it

enum Priority {
    LOW("Can wait"), MEDIUM("Plan soon"), HIGH("Do now");

    private final String label;

    Priority(String label) {
        this.label = label;
    }

    String label() {
        return label;
    }
}

record Task(String title, List<String> topics, Priority priority) {
    Task(Task other) {
        this(other.title, new ArrayList<>(other.topics), other.priority);
    }
}

// a deep copy duplicates mutable nested state instead of sharing it

record CloneableTopics(List<String> values) implements Cloneable {
    @Override
    public CloneableTopics clone() {
        try {
            return (CloneableTopics) super.clone();
        } catch (CloneNotSupportedException exception) {
            throw new AssertionError(exception);
        }
    }
}

// Object.clone performs a field-by-field shallow copy and requires the Cloneable marker interface
