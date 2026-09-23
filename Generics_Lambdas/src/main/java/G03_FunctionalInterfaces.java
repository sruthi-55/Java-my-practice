import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.IntFunction;
import java.util.ArrayList;
import java.util.List;

// Consumer accepts input, Predicate tests input, Function transforms input and Supplier creates output
// functional interface contains one abstract method and may contain default and static methods
// lambda parameters need a target functional type and captured local variables must be final or effectively final
// static references use Type::method, bound references use instance::method and unbound references use Type::instanceMethod
// constructor references use Type::new and array constructor references use ElementType[]::new

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

        // static, bound-instance, unbound-instance and constructor references adapt compatible signatures
        Function<String, Integer> parse = Integer::parseInt;
        Predicate<String> startsWithJava = "Java21"::startsWith;
        Supplier<List<String>> create = ArrayList::new;
        IntFunction<String[]> array = String[]::new;
        System.out.println(parse.apply("21"));	// 21
        System.out.println(startsWithJava.test("Java"));	// true
        System.out.println(create.get().isEmpty());	// true
        System.out.println(array.apply(2).length);	// 2

        // compose runs its argument first while andThen runs its argument after this function
        Function<Integer, Integer> doubleValue = number -> number * 2;
        Function<Integer, Integer> addOne = number -> number + 1;
        System.out.println(doubleValue.compose(addOne).apply(3));	// 8
        System.out.println(doubleValue.andThen(addOne).apply(3));	// 7

        // predicate composition short-circuits and can safely guard a nullable argument
        Predicate<String> nonNull = java.util.Objects::nonNull;
        System.out.println(nonNull.and(text -> text.length() > 3).test(null));	// false
        System.out.println(isEven.negate().or(number -> number > 10).test(3));	// true

        // capture freezes the local reference but does not freeze its mutable object
        List<String> captured = new ArrayList<>();
        Runnable append = () -> captured.add("Java");
        append.run();
        System.out.println(captured);	// [Java]
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
