import java.util.Objects;

// an object is a class instance or array with identity and associated state
// a reference identifies an object or holds null and multiple references can identify the same object
// Object is the root class while Objects is a non-instantiable utility class with static helper methods
// primitives are not objects but boxing lets primitive values be represented by wrapper objects

public class OB00_ObjectIntroduction {
    public static void main(String[] args) {
        // default Object equality compares identity rather than matching field values
        Object first = new Object();
        Object alias = first;
        Object second = new Object();
        System.out.println(first == alias);	// true
        System.out.println(first.equals(second));	// false
        System.out.println(Objects.equals(first, alias));	// true

        // runtime types remain specific even when references are declared as Object
        Object boxed = 21;
        Object array = new int[]{1, 2};
        System.out.println(boxed.getClass().getSimpleName());	// Integer
        System.out.println(array.getClass().isArray());	// true
        System.out.println(array instanceof Object);	// true

        // pattern matching safely exposes subtype behavior through an Object reference
        Object value = "Java";
        if (value instanceof String text) {
            System.out.println(text.length());	// 4
        }
        System.out.println(null instanceof Object);	// false
    }

    // Object has no superclass and every other class ultimately inherits from it
    // interfaces do not extend Object although their implementing class instances are objects
    // a reference's declared type determines accessible members while overrides dispatch by runtime type
    // assigning null to one reference does not destroy an object still reachable through another reference
}
