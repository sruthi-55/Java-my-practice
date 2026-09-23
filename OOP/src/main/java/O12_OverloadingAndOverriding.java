import java.io.IOException;
import java.io.FileNotFoundException;

// overloading selects a method signature at compile time while overriding dispatches an instance method at runtime
// widening is considered before boxing and fixed-arity calls before variable-arity calls

public class O12_OverloadingAndOverriding {
    public static void main(String[] args) throws IOException {
        System.out.println(choose(1));	// long
        System.out.println(choose(Integer.valueOf(1)));	// Integer
        System.out.println(choose(1, 2));	// varargs
        System.out.println(describe((Object) "Java"));	// Object
        System.out.println(describe("Java"));	// String
        System.out.println(describe(null));	// String

        Base base = new Child();
        System.out.println(base.copy().getClass().getSimpleName());	// Child
        System.out.println(base.read());	// child
        System.out.println(base.privateCaller());	// base private
        System.out.println(base.kind());	// base static
        System.out.println(Child.kind());	// child static
    }

    static String choose(long value) { return "long"; }
    static String choose(Integer value) { return "Integer"; }
    static String choose(int... values) { return "varargs"; }
    static String describe(Object value) { return "Object"; }
    static String describe(String value) { return "String"; }

    static class Base {
        protected Base copy() { return new Base(); }
        protected String read() throws IOException { return "base"; }
        private String secret() { return "base private"; }
        String privateCaller() { return secret(); }
        static String kind() { return "base static"; }
    }

    static class Child extends Base {
        // an override may widen access, narrow checked exceptions and return a covariant reference type
        @Override
        public Child copy() { return new Child(); }
        @Override
        public String read() throws FileNotFoundException { return "child"; }
        private String secret() { return "child private"; }
        static String kind() { return "child static"; }
    }

    // private methods are not overridden and static methods are hidden rather than dynamically dispatched
    // return type alone cannot distinguish overloads and unrelated reference overloads make a null call ambiguous
    // primitive return types must match in overrides and checked exceptions cannot be broadened
    // call static methods through their class names; the reference-style call above deliberately demonstrates hiding
    // varargs must be last and int[] cannot coexist with int... as another overload of the same method
}
