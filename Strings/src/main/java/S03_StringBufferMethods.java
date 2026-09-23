// StringBuffer is a mutable character sequence with synchronized operations for shared mutation
// default capacity is 16 and a String argument creates capacity equal to length plus 16

public class S03_StringBufferMethods {
    public static void main(String[] args) throws InterruptedException {
        StringBuffer buffer = new StringBuffer("Hello");
        System.out.println(buffer.capacity());	// 21

        buffer.append(" Sruthi");
        System.out.println(buffer);	// Hello Sruthi
        buffer.insert(5, ',');
        System.out.println(buffer);	// Hello, Sruthi
        buffer.replace(0, 5, "Hi");
        System.out.println(buffer);	// Hi, Sruthi
        buffer.delete(2, 4);
        System.out.println(buffer);	// HiSruthi
        buffer.reverse();
        System.out.println(buffer);	// ihturSiH
        System.out.println(buffer.length());	// 8
        System.out.println(buffer.charAt(0));	// i
        buffer.setCharAt(0, 'I');
        System.out.println(buffer);	// IhturSiH
        buffer.deleteCharAt(0);
        System.out.println(buffer.substring(0, 3));	// htu

        StringBuffer growing = new StringBuffer();
        growing.ensureCapacity(20);
        growing.append("12345678901234567");
        System.out.println(growing.capacity());	// 34
        growing.trimToSize();
        System.out.println(growing.capacity());	// 17

        // individually synchronized calls do not make a check-then-act sequence atomic
        StringBuffer shared = new StringBuffer();
        Runnable appendOnce = () -> {
            synchronized (shared) {
                if (shared.isEmpty()) {
                    shared.append("Java");
                }
            }
        };
        Thread first = new Thread(appendOnce);
        Thread second = new Thread(appendOnce);
        first.start();
        second.start();
        first.join();
        second.join();
        System.out.println(shared);	// Java
    }

    // capacity grows when required and is normally calculated as old capacity times two plus two
}
