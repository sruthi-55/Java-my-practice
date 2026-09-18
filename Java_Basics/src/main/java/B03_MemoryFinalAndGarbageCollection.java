import java.lang.ref.Cleaner;

// stack frames normally hold method calls and local variables while objects normally live in the heap
// final prevents variable reassignment, method overriding or class inheritance depending on its target
// garbage collection reclaims unreachable objects but does not provide deterministic cleanup timing
// AutoCloseable and try-with-resources provide deterministic resource cleanup

public class B03_MemoryFinalAndGarbageCollection {
    private static final Cleaner CLEANER = Cleaner.create();

    public static void main(String[] args) {
        // final reference cannot be reassigned but its referenced mutable object can change
        final MutableValue value = new MutableValue(10);
        value.number = 20;
        System.out.println(value.number);	// 20

        // try-with-resources closes an AutoCloseable resource automatically
        try (Resource resource = new Resource()) {
            System.out.println("resource in use");	// resource in use
        }

        FinalChild child = new FinalChild(10);
        System.out.println(child.value());	// 10
    }

    // a reference field belongs to its containing object and therefore may itself be stored in the heap
    // a blank final field may be initialized by a constructor rather than at its declaration
    // Java does not provide deterministic object destruction and garbage-collection timing is not guaranteed
    // finalize is deprecated for removal and must not be used for resource cleanup

    static final class Resource implements AutoCloseable {
        private final Cleaner.Cleanable cleanable = CLEANER.register(this, () -> System.out.println("fallback cleanup"));	// fallback cleanup

        @Override
        public void close() {
            cleanable.clean();
        }
    }
}

class FinalParent {
    final int value;

    FinalParent(int value) {
        this.value = value;
    }

    final int value() {
        return value;
    }
}

// final class cannot be inherited and final method cannot be overridden
final class FinalChild extends FinalParent {
    FinalChild(int value) {
        super(value);
    }
}

class MutableValue {
    int number;

    MutableValue(int number) {
        this.number = number;
    }
}
