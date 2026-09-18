import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

// Consumer accepts input, Predicate tests input, Function transforms input and Supplier creates output
// functional interface contains one abstract method and may contain default and static methods

public class G03_FunctionalInterfaces {
    public static void main(String[] args) {
        Predicate<Integer> isEven = number -> number % 2 == 0;
        Function<String, Integer> length = String::length;
        Consumer<String> printer = System.out::println;
        Supplier<String> value = () -> "Java";

        printer.accept(value.get());	// Java
        printer.accept("Length: " + length.apply(value.get()));	// Length: 4
        printer.accept("Even: " + isEven.test(length.apply(value.get())));	// Even: true

        Formatter formatter = text -> text.trim().toUpperCase();
        System.out.println(formatter.decorate(" java "));	// [JAVA]
        System.out.println(Formatter.identity("SQL"));	// SQL
    }
}

@FunctionalInterface
interface Formatter {
    String format(String value);

    default String decorate(String value) {
        return "[" + format(value) + "]";
    }

    static String identity(String value) {
        return value;
    }
}
