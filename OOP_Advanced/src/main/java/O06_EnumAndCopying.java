import java.util.ArrayList;
import java.util.List;

// enum defines a fixed set of named instances and can contain fields, constructors and methods
// shallow copy shares nested object references while deep copy duplicates mutable nested state

public class O06_EnumAndCopying {
    public static void main(String[] args) {
        Task original = new Task("Prepare Java", new ArrayList<>(List.of("OOP")), Priority.HIGH);
        Task copy = new Task(original);
        original.topics().add("Collections");

        System.out.println(original);	// Task with [OOP, Collections]
        System.out.println(copy);	// Task with [OOP]
        System.out.println(Priority.HIGH.label());	// Do now

        CloneableTopics shallowOriginal = new CloneableTopics(new ArrayList<>(List.of("Java")));
        CloneableTopics shallowCopy = shallowOriginal.clone();
        shallowOriginal.values().add("SQL");
        System.out.println(shallowCopy.values());	// [Java, SQL]
    }
}

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
