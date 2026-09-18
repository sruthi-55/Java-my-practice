import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Vector;

// cursor is an object used to traverse collection elements one at a time
// Enumeration is a legacy forward-only cursor mainly used with Vector and Hashtable
// Iterator is a universal forward-only cursor for collections and supports optional removal
// ListIterator is a bidirectional cursor available only for List implementations
// Iterable provides iterator() and enables the enhanced for loop

public class C04_Cursors {
    public static void main(String[] args) {
        iteratorMethods();
        listIteratorMethods();
        enumerationMethods();
        iteratorExceptions();
    }

    static void iteratorMethods() {
        List<String> skills = new ArrayList<>(List.of("Java", "SQL", "Git"));
        Iterator<String> iterator = skills.iterator();

        System.out.println(iterator.hasNext());	// true
        System.out.println(iterator.next());	// Java

        while (iterator.hasNext()) {
            if (iterator.next().equals("SQL")) iterator.remove();
        }

        System.out.println(skills);	// [Java, Git]
    }

    static void listIteratorMethods() {
        List<String> values = new ArrayList<>(List.of("A", "C"));
        ListIterator<String> iterator = values.listIterator();

        System.out.println(iterator.hasNext());	// true
        System.out.println(iterator.next());	// A
        System.out.println(iterator.nextIndex());	// 1
        System.out.println(iterator.previousIndex());	// 0

        iterator.add("B");
        System.out.println(values);	// [A, B, C]

        System.out.println(iterator.next());	// C
        iterator.set("D");
        System.out.println(values);	// [A, B, D]

        System.out.println(iterator.hasPrevious());	// true
        System.out.println(iterator.previous());	// D
        iterator.remove();
        System.out.println(values);	// [A, B]
    }

    static void enumerationMethods() {
        Vector<String> legacyValues = new Vector<>(List.of("old-one", "old-two"));
        Enumeration<String> enumeration = legacyValues.elements();

        System.out.println(enumeration.hasMoreElements());	// true
        System.out.println(enumeration.nextElement());	// old-one
        System.out.println(enumeration.nextElement());	// old-two
    }

    static void iteratorExceptions() {
        // NoSuchElementException occurs when next() is called after all elements are consumed
        Iterator<Integer> exhausted = List.of(1).iterator();
        exhausted.next();
        try {
            exhausted.next();
        } catch (NoSuchElementException exception) {
            System.out.println(exception.getClass().getSimpleName());	// NoSuchElementException
        }

        // IllegalStateException occurs when remove() is called before next() selects an element
        Iterator<Integer> illegalRemove = new ArrayList<>(List.of(1)).iterator();
        try {
            illegalRemove.remove();
        } catch (IllegalStateException exception) {
            System.out.println(exception.getClass().getSimpleName());	// IllegalStateException
        }

        // UnsupportedOperationException occurs when remove() is called on an unmodifiable iterator
        Iterator<Integer> unsupportedRemove = List.of(1).iterator();
        unsupportedRemove.next();
        try {
            unsupportedRemove.remove();
        } catch (UnsupportedOperationException exception) {
            System.out.println(exception.getClass().getSimpleName());	// UnsupportedOperationException
        }

        // ConcurrentModificationException occurs when a fail-fast iterator detects structural modification
        List<Integer> modifiedDirectly = new ArrayList<>(List.of(1, 2));
        Iterator<Integer> failFast = modifiedDirectly.iterator();
        modifiedDirectly.add(3);
        try {
            failFast.next();
        } catch (ConcurrentModificationException exception) {
            System.out.println(exception.getClass().getSimpleName());	// ConcurrentModificationException
        }
    }

    // next throws NoSuchElementException when no element remains
    // remove throws IllegalStateException before next or when called twice for the same element
    // remove throws UnsupportedOperationException when the iterator does not support modification
    // structural modification outside the iterator can cause ConcurrentModificationException on a fail-fast iterator
    // fail-fast collections track structural changes with modCount and iterators compare it with expectedModCount
    // ConcurrentModificationException is best-effort bug detection and must not be used for program correctness
}
