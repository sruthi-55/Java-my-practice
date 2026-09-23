import access.base.AccessBase;
import access.client.AccessChild;
import java.util.ArrayList;
import java.util.List;

// access control limits visibility while final restricts reassignment, overriding or inheritance
// final references may still refer to mutable objects

public class O14_AccessAndFinal {
    public static void main(String[] args) {
        AccessBase base = new AccessBase();
        AccessChild child = new AccessChild();
        System.out.println(base.publicValue);	// 1
        System.out.println(child.inheritedProtected());	// 2
        System.out.println(child.anotherChild(new AccessChild()));	// 2
        System.out.println(AccessBase.samePackageAccess());	// 5
        System.out.println(base.privateThroughMethod());	// 4

        final List<String> skills = new ArrayList<>();
        skills.add("Java");
        System.out.println(skills);	// [Java]
        System.out.println(new FixedValue(42).get());	// 42
    }

    static class FixedBase {
        private final int value;
        FixedBase(int value) { this.value = value; }
        final int get() { return value; }
    }

    static final class FixedValue extends FixedBase {
        FixedValue(int value) { super(value); }
    }

    // a blank final field must be assigned exactly once on every successful constructor path
    // final classes cannot be extended and final methods cannot be overridden
    // abstract and final conflict because an abstract implementation requires extension
    // packages organize names and a subpackage does not gain access to its parent's package-private members
    // top-level classes are public or package-private while nested member types support all access modifiers
}
