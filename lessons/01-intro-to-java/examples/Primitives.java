/**
 * Lesson 1: the eight primitive types.
 *
 * Compile: javac Primitives.java
 * Run:     java Primitives
 */
public class Primitives {

    public static void main(String[] args) {
        // Whole numbers
        byte b = 100;
        short s = 30_000;                  // underscores make big numbers easier to read
        int i = 2_000_000_000;
        long l = 9_000_000_000L;           // L is needed: the number is too big for an int

        // Decimal numbers
        float f = 3.5f;                    // f is needed: 3.5 on its own is a double
        double d = 3.14159265358979;

        // Other types
        char c = 'J';                      // single quotes for one character
        boolean ok = true;

        System.out.println("byte:    " + b);
        System.out.println("short:   " + s);
        System.out.println("int:     " + i);
        System.out.println("long:    " + l);
        System.out.println("float:   " + f);
        System.out.println("double:  " + d);
        System.out.println("char:    " + c);
        System.out.println("boolean: " + ok);

        // Each type has limits. The wrapper classes (Lesson 2) tell you what they are.
        System.out.println();
        System.out.println("Largest int:  " + Integer.MAX_VALUE);
        System.out.println("Smallest int: " + Integer.MIN_VALUE);
        System.out.println("Largest long: " + Long.MAX_VALUE);

        // Going past the largest int "wraps around" to the most negative int.
        // This is called overflow, and Java does NOT warn you about it.
        int biggest = Integer.MAX_VALUE;
        int overflow = biggest + 1;
        System.out.println();
        System.out.println("Integer.MAX_VALUE + 1 = " + overflow);

        // A float keeps only about 7 significant digits, so it can lose precision
        int n = 123456789;
        float asFloat = n;
        System.out.println("123456789 stored as a float: " + asFloat);
    }
}
