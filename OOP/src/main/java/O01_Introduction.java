public class O01_Introduction {
    public static void main(String[] args) {
        int[] rollNums = new int[5];
        String[] names = new String[5];
        float[] marks = new float[5];

        Student[] students = new Student[5];
        Student student1 = new Student(1,"Sruthi Mora",99);
        Student student2 = new Student();
        student2.marks = 55;

        Student random = new Student(student1);

        System.out.println("Student 1: "+student1.rno+","+student1.name+","+student1.marks);
        System.out.println("Student 2: "+student2.rno+","+student2.name+","+student2.marks);
        System.out.println("Random student: "+random.rno+","+random.name+","+random.marks);
    }
}

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
        System.out.println("Hi "+this.name);
    }

    void changeName(String newName){
        this.name = newName;
    }
}
