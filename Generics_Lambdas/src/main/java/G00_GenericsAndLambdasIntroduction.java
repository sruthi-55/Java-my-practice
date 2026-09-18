import java.util.function.Function;

// generics provide compile-time type safety by parameterizing classes, interfaces and methods
// lambda expression supplies an implementation for a functional interface with one abstract method
// method reference is concise lambda syntax that refers to an existing compatible method

public class G00_GenericsAndLambdasIntroduction {
    public static void main(String[] args) {
        Pair<String, Integer> skill = new Pair<>("Java", 2);
        System.out.println(skill.first() + "-" + skill.second());	// Java-2

        Function<String, Integer> lambda = value -> value.length();
        Function<String, Integer> methodReference = String::length;
        System.out.println(lambda.apply("Java"));	// 4
        System.out.println(methodReference.apply("Spring"));	// 6
    }
}

record Pair<K, V>(K first, V second) {
}
