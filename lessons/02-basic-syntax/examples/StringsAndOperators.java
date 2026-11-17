/**
 * Lesson 2: operators and Strings.
 *
 * Compile: javac StringsAndOperators.java
 * Run:     java StringsAndOperators
 */
public class StringsAndOperators {

    public static void main(String[] args) {
        // ---------- Number operators ----------
        System.out.println("7 / 2       = " + (7 / 2));       // int division: 3
        System.out.println("7.0 / 2     = " + (7.0 / 2));     // 3.5
        System.out.println("7 % 2       = " + (7 % 2));       // remainder: 1
        double d = 7 / 2;                                     // still int division
        System.out.println("double d = 7 / 2 gives " + d);    // 3.0

        // ---------- Booleans ----------
        int x = 60;
        boolean inRange = x > 45 && x < 100;
        System.out.println("x is between 45 and 100: " + inRange);

        // ---------- String concatenation: left to right ----------
        int age = 20;
        System.out.println("John will be " + age + 1 + " years old.");     // 201
        System.out.println("John will be " + (age + 1) + " years old.");   // 21

        // ---------- Strings never change ----------
        String s = "hello";
        s.toUpperCase();                    // the result is thrown away
        System.out.println("After s.toUpperCase(): " + s);
        s = s.toUpperCase();                // keep the new String
        System.out.println("After s = s.toUpperCase(): " + s);

        // ---------- String methods ----------
        String name = "Simon";
        System.out.println("length:          " + name.length());
        System.out.println("charAt(0):       " + name.charAt(0));
        System.out.println("indexOf(\"mon\"):  " + name.indexOf("mon"));
        System.out.println("indexOf(\"z\"):    " + name.indexOf("z"));
        System.out.println("substring(1, 3): " + name.substring(1, 3));

        // ---------- The parsing example from the lecture ----------
        String full = "lincoln, abraham";
        int comma = full.indexOf(',');
        String last = full.substring(0, comma).trim();
        String first = full.substring(comma + 1).trim();
        first = first.substring(0, 1).toUpperCase() + first.substring(1);
        last = last.substring(0, 1).toUpperCase() + last.substring(1);
        System.out.println(first + " " + last);

        // ---------- equals vs == ----------
        String a = "yes";
        String b = new String("yes");       // a different object with the same text
        System.out.println("a.equals(b): " + a.equals(b));   // true: same text
        System.out.println("a == b:      " + (a == b));      // false: different objects

        // ---------- Casting ----------
        int chopped = (int) 9.99;
        System.out.println("(int) 9.99 = " + chopped);
    }
}
