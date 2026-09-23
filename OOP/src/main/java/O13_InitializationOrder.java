// class initialization runs parent static initialization before child static initialization once per class initialization
// construction runs parent initialization before child fields, instance blocks and constructor body

public class O13_InitializationOrder {
    public static void main(String[] args) {
        new Child();
        // calling an overridable method from a constructor can expose a child's default field values
        new EarlyChild();
        Defaults defaults = new Defaults();
        System.out.println(defaults.number + " " + defaults.flag + " " + defaults.name);	// 0 false null

        // reading a compile-time constant does not initialize its declaring class
        System.out.println(Lazy.CONSTANT);	// 21
        System.out.println(initializations);	// 0
        System.out.println(Lazy.runtimeValue);	// 42
        System.out.println(initializations);	// 1
    }

    static int initializations;
    static class Lazy {
        static final int CONSTANT = 21;
        static final int runtimeValue = initialize();
        static int initialize() { initializations++; return 42; }
    }

    static class Parent {
        static { System.out.println("parent static"); }	// parent static
        { System.out.println("parent instance"); }	// parent instance
        Parent() { System.out.println("parent constructor"); }	// parent constructor
    }

    static class Child extends Parent {
        static { System.out.println("child static"); }	// child static
        int value = initialize();
        { System.out.println("child instance"); }	// child instance
        Child() { System.out.println("child constructor"); }	// child constructor
        int initialize() {
            System.out.println("child field");	// child field
            return 1;
        }
    }

    static class EarlyParent {
        EarlyParent() { show(); }
        void show() { }
    }

    static class EarlyChild extends EarlyParent {
        int value = 10;
        @Override
        void show() { System.out.println(value); }	// 0
    }

    // the compiler supplies this no-argument constructor because no constructor is declared
    static class Defaults {
        int number;
        boolean flag;
        String name;
    }

    // fields and instance initializer blocks execute in textual order after super returns
    // Java 21 requires this(...) or super(...) first and constructor chains cannot be recursive
    // constructors are not inherited, overridden, static or final and an implicit super() needs an accessible parent constructor
    // static methods have no this and cannot directly access instance members
}
