// StringBuffer is a mutable synchronized character sequence suited to shared multi-threaded mutation
// default capacity is 16 and a String argument creates capacity equal to length plus 16

public class S03_StringBufferMethods {
    public static void main(String[] args) {
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
    }

    // capacity grows when required and is normally calculated as old capacity times two plus two
}
