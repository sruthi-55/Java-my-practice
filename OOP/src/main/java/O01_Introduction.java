// class is a user-defined type that defines the state and behavior of its objects
// object is a class instance with state, behavior and identity
// constructor initializes a new object and has no return type
// this refers to the current object and this(...) invokes another constructor

public class O01_Introduction {
    public static void main(String[] args) {
        int[] rollNums = new int[5];
        String[] names = new String[5];
        float[] marks = new float[5];

        Student[] students = new Student[5];
        // arrays and fields receive defaults while local variables require definite assignment
        System.out.println(rollNums[0] + " " + names[0] + " " + marks[0] + " " + students[0]);	// 0 null 0.0 null
        Student student1 = new Student(1,"Sruthi Mora",99);
        Student student2 = new Student();
        student2.marks = 55;

        Student random = new Student(student1);

        System.out.println("Student 1: "+student1.rno+","+student1.name+","+student1.marks);	// Student 1: 1,Sruthi Mora,99.0
        System.out.println("Student 2: "+student2.rno+","+student2.name+","+student2.marks);	// Student 2: 0,Default student,55.0
        System.out.println("Random student: "+random.rno+","+random.name+","+random.marks);	// Random student: 1,Sruthi Mora,99.0
        // assignment aliases one object while the copy constructor creates another object
        Student alias = student1;
        alias.changeName("Sruthi");
        alias.greeting();
        System.out.println(alias == student1);	// true
        System.out.println(random == student1);	// false
    }
}

// a compiler-provided default constructor exists only when no constructor is declared
// an explicit no-argument constructor is not the same as the compiler-provided default constructor

class Student{
    int rno;
    String name;
    float marks;

    Student(){
        this(0,"Default student",0.0f);
    }

    Student(int rno, String name, float marks){
        this.rno = rno;
        this.name = name;
        this.marks = marks;
    }

    Student(Student other){
        this.rno = other.rno;
        this.name = other.name;
        this.marks = other.marks;
    }

    void greeting(){
        System.out.println("Hi "+this.name);	// Hi followed by the student name
    }

    void changeName(String newName){
        this.name = newName;
    }
}
