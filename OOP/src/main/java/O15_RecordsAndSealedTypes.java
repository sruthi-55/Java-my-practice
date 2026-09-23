import java.util.ArrayList;
import java.util.List;

// records declare data carriers with component accessors and generated equals, hashCode and toString
// sealed types restrict direct subtypes and each permitted class continues as final, sealed or non-sealed

public class O15_RecordsAndSealedTypes {
    public static void main(String[] args) {
        List<String> original = new ArrayList<>(List.of("Java"));
        Candidate candidate = new Candidate("Sruthi", original);
        original.add("SQL");
        System.out.println(candidate);	// Candidate[name=Sruthi, skills=[Java]]
        System.out.println(candidate.equals(new Candidate("Sruthi", List.of("Java"))));	// true
        System.out.println(candidate.name());	// Sruthi
        System.out.println(area(new Square(3)));	// 9
        System.out.println(area(new Rectangle(2, 4)));	// 8
        System.out.println(new ColoredExtension() instanceof Shape);	// true
    }

    record Candidate(String name, List<String> skills) {
        Candidate {
            if (name == null || name.isBlank()) throw new IllegalArgumentException("name required");
            skills = List.copyOf(skills);
        }
    }

    sealed interface Shape permits Square, Rectangle, Extension { }
    record Square(int side) implements Shape { }
    record Rectangle(int width, int height) implements Shape { }
    static non-sealed class Extension implements Shape { }
    static final class ColoredExtension extends Extension { }

    // Java 21 type patterns make this switch exhaustive over the permitted hierarchy
    static int area(Shape shape) {
        return switch (shape) {
            case Square square -> square.side() * square.side();
            case Rectangle rectangle -> rectangle.width() * rectangle.height();
            case Extension extension -> 0;
        };
    }

    // records are implicitly final and only shallowly immutable unless mutable components are defensively copied
    // a record extends Record and may implement interfaces but cannot extend another class
    // permitted subclasses must share the named module or the package in an unnamed module
    // instanceof patterns narrow a reference safely and null never matches an instanceof type pattern
}
