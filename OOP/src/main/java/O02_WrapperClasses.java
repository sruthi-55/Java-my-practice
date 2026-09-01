public class O02_WrapperClasses {
    public static void main(String[] args) {
        int a = 2, b = 3;
        System.out.println("Before swapping integers: "+a+" "+b);
        swapInt(a,b);
        System.out.println("After swapping integers: "+a+" "+b);

        Integer A = 22, B = 33;
        System.out.println("After swapping Integers: "+A+" "+B);
        swapIntegers(A,B);
        System.out.println("After swapping Integers: "+A+" "+B);

        // primitives are passed by value
        // objects are passed by reference

        // still Integers are not swapped because wrapper classes are final classes
        // with final we can prevent content to be modified

        // final int bonus = 3;
        // bonus = 3  // can't redefine
        // if objs are made final, obj reference can’t be changed but value can be changed

        // garbage collection
        GC obj = new GC();
        for(int i=0;i<1000000;i++){
            obj = new GC();
        }
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

class GC{
    final int myVal = 23;

    @Override
    protected void finalize() throws Throwable{
        System.out.println("Object is destroyed");
    }
}