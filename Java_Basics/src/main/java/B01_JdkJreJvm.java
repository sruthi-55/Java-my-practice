// JDK provides tools and libraries used to develop, compile, debug, document and package Java applications
// JRE provides the JVM and runtime libraries required to execute Java applications
// JVM loads, verifies and executes platform-independent bytecode on the current operating system
// JVM execution proceeds through loading, linking, initialization and execution

public class B01_JdkJreJvm {
    public static void main(String[] args) {
        System.out.println("Source -> javac -> bytecode -> JVM -> native execution");	// Source -> javac -> bytecode -> JVM -> native execution
        System.out.println(System.getProperty("java.version"));	// installed Java version
        System.out.println(System.getProperty("java.vm.name"));	// installed JVM name
    }

    // Java is a high-level object-oriented language with a rich standard library
    // bytecode is platform-independent because each platform provides a compatible JVM
    // the JDK provides javac, java, jar, javadoc, jdb, libraries and the runtime needed for development
    // the JRE conceptually contains the JVM, Java class libraries and supporting runtime libraries
    // linking includes bytecode verification, preparation of static storage and symbolic-reference resolution
    // the interpreter executes bytecode while JIT compiles frequently executed code into native instructions
    // the JVM manages runtime memory, garbage collection, security and execution services
    // Spring and Hibernate are popular frameworks built on the Java ecosystem
}
