// constructor initializes an object and can be no-argument, parameterized, private or user-defined copy constructor
// inner class requires an outer instance while static nested class does not
// Object is the root class and supplies methods such as toString, equals, hashCode and getClass

public class O09_ConstructorsInnerClassesAndObject {
    public static void main(String[] args) {
        ConstructorTypes defaultValue = new ConstructorTypes();
        ConstructorTypes parameterized = new ConstructorTypes(10);
        ConstructorTypes copy = new ConstructorTypes(parameterized);
        System.out.println(defaultValue + " " + copy);	// ConstructorTypes[value=0] ConstructorTypes[value=10]
        System.out.println(copy.getClass() + " " + (copy instanceof Object));	// class ConstructorTypes true
        System.out.println(new Outer().new Inner().value());	// 1
        System.out.println(Outer.StaticInner.value());	// 2
        System.out.println(overload(1) + " " + overload("Java"));	// int String
        new ConstructorChild();
    }

    static String overload(int value) {
        return "int";
    }

    static String overload(String value) {
        return "String";
    }

    // this refers to the current object and this(...) invokes another constructor as the first statement
    // super refers to the immediate parent and super(...) invokes its constructor before subclass initialization
    // constructor execution proceeds from Object down through parent classes to the concrete child
    // overloading changes parameter number, type or order and is selected at compile time without requiring inheritance
    // every class ultimately extends Object and inherits toString, equals, hashCode, getClass, wait, notify and notifyAll
    // top-level classes cannot be static while a static nested class does not require an outer instance
}

class ConstructorParent {
    ConstructorParent() {
        System.out.println("parent constructor");	// parent constructor
    }
}

class ConstructorChild extends ConstructorParent {
    ConstructorChild() {
        super();
        System.out.println("child constructor");	// child constructor
    }
}

class ConstructorTypes {
    private final int value;

    ConstructorTypes() {
        this(0);
    }

    ConstructorTypes(int value) {
        this.value = value;
    }

    ConstructorTypes(ConstructorTypes other) {
        this(other.value);
    }

    @Override
    public String toString() {
        return "ConstructorTypes[value=" + value + "]";
    }
}

class Outer {
    class Inner {
        int value() {
            return 1;
        }
    }

    static class StaticInner {
        static int value() {
            return 2;
        }
    }
}
