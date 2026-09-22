// finally runs when control leaves try or catch through normal completion, return or a thrown exception
// abrupt completion of finally replaces a pending return or exception and should be avoided

public class E06_FinallyBehavior {
    public static void main(String[] args) {
        System.out.println(savedReturn());	// 1
        System.out.println(overriddenReturn());	// 2
        System.out.println(swallowedFailure());	// 3
        System.out.println(mutableReturn());	// Java revised

        // finally also runs when continue or break exits the try block
        for (int index = 0; index < 2; index++) {
            try {
                if (index == 0) continue;
                break;
            } finally {
                System.out.println(index);	// 0, then 1
            }
        }

        // a finally failure hides the original exception without automatic suppression
        try {
            try {
                throw new IllegalArgumentException("original failure");
            } finally {
                throw new IllegalStateException("cleanup failure");
            }
        } catch (IllegalStateException exception) {
            System.out.println(exception.getMessage());	// cleanup failure
            System.out.println(exception.getSuppressed().length);	// 0
        }
    }

    // return expression is evaluated before finally changes the local variable
    static int savedReturn() {
        int value = 1;
        try {
            return value;
        } finally {
            value = 2;
        }
    }

    // finally can mutate the returned object but reassigning the local reference does not replace it
    static StringBuilder mutableReturn() {
        StringBuilder value = new StringBuilder("Java");
        try {
            return value;
        } finally {
            value.append(" revised");
            value = new StringBuilder("replacement");
        }
    }

    // intentional bad example shows why return inside finally must be avoided
    static int overriddenReturn() {
        try {
            return 1;
        } finally {
            return 2;
        }
    }

    // intentional bad example shows a return from finally silently discarding a failure
    static int swallowedFailure() {
        try {
            throw new IllegalStateException("lost failure");
        } finally {
            return 3;
        }
    }

    // System.exit, Runtime.halt, process termination or non-termination can prevent finally from running
    // prefer try-with-resources when cleanup can itself fail
}
