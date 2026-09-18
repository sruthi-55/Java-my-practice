// wrapper classes represent primitive values as immutable objects and support generics and utility methods
// Java always passes arguments by value and object arguments copy the reference value
// reassigning a method parameter never reassigns the caller's variable

public class O02_WrapperClasses {
    public static void main(String[] args) {
        int a = 2, b = 3;
        System.out.println("Before swapping integers: "+a+" "+b);	// Before swapping integers: 2 3
        swapInt(a,b);
        System.out.println("After swapping integers: "+a+" "+b);	// After swapping integers: 2 3

        Integer A = 22, B = 33;
        System.out.println("Before swapping Integers: "+A+" "+B);	// Before swapping Integers: 22 33
        swapIntegers(A,B);
        System.out.println("After swapping Integers: "+A+" "+B);	// After swapping Integers: 22 33

        Integer parsed = Integer.valueOf("42");
        System.out.println(parsed);	// 42

        // autoboxing converts a primitive to a wrapper and unboxing converts it back
        Integer boxed = 10;
        int unboxed = boxed;
        System.out.println(boxed + " " + unboxed);	// 10 10

        System.out.println(Integer.parseInt("21"));	// 21
        System.out.println(Integer.toString(21));	// 21
        System.out.println(Integer.compare(10, 20));	// -1
    }
    static void swapInt(int a, int b){
        int temp = a;
        a = b;
        b = temp;
    }

    static void swapIntegers(Integer a, Integer b){
        Integer temp = a;
        a = b;
        b = temp;
    }
}
