import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// equals defines logical equality and hashCode must return equal values for equal objects
// immutable object prevents observable state changes after construction
// defensive copy prevents callers from mutating internal mutable state

public class O04_ObjectEqualityAndImmutability {
    public static void main(String[] args) {
        User first = new User(1, "Sruthi");
        User second = new User(1, "Sruthi");
        System.out.println(first == second);	// false
        System.out.println(first.equals(second));	// true
        System.out.println(first.hashCode() == second.hashCode());	// true

        List<String> skills = new ArrayList<>(List.of("Java"));
        Profile profile = new Profile("Sruthi", skills);
        skills.add("SQL");
        System.out.println(profile.skills());	// [Java]
    }
}

final class User {
    private final int id;
    private final String name;

    User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof User user)) return false;
        return id == user.id && Objects.equals(name, user.name);
    }

    // equal objects must always return the same hash code
    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}

record Profile(String name, List<String> skills) {
    Profile {
        skills = List.copyOf(skills);
    }

    // defensive copies stop callers from mutating internal state
    @Override
    public List<String> skills() {
        return List.copyOf(skills);
    }
}
