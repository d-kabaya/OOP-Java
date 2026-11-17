import java.util.ArrayList;

/**
 * Lesson 2: decisions, loops, arrays and wrapper classes.
 *
 * Compile: javac ControlFlow.java
 * Run:     java ControlFlow
 */
public class ControlFlow {

    public static void main(String[] args) {
        // ---------- if / else if / else ----------
        int mark = 55;
        if (mark >= 70) {
            System.out.println(mark + " is a First");
        } else if (mark >= 40) {
            System.out.println(mark + " is a Pass");
        } else {
            System.out.println(mark + " is a Fail");
        }

        // ---------- switch, with break ----------
        int day = 2;
        switch (day) {
            case 1:
                System.out.println("Monday");
                break;
            case 2:
                System.out.println("Tuesday");
                break;
            default:
                System.out.println("Another day");
        }

        // ---------- switch WITHOUT break: falls through ----------
        System.out.println("Fall-through example:");
        day = 1;
        switch (day) {
            case 1:
                System.out.println("  Mon");
            case 2:
                System.out.println("  Tue");
                break;
            case 3:
                System.out.println("  Wed");
        }

        // ---------- while ----------
        int count = 0;
        while (count < 3) {
            System.out.println("while: count is " + count);
            count++;
        }

        // ---------- for, over an array ----------
        int[] primes = {2, 3, 5, 7};
        for (int i = 0; i < primes.length; i++) {
            System.out.println("primes[" + i + "] = " + primes[i]);
        }

        // ---------- for-each ----------
        int total = 0;
        for (int p : primes) {
            total += p;
        }
        System.out.println("Sum of primes: " + total);

        // ---------- A new array starts with default values ----------
        int[] empty = new int[3];
        System.out.println("new int[3] holds: " + empty[0] + ", " + empty[1] + ", " + empty[2]);

        // ---------- ArrayList grows as you add ----------
        ArrayList<Integer> list = new ArrayList<Integer>();
        list.add(5);
        list.add(8);
        list.add(13);
        System.out.println("ArrayList: " + list + ", size " + list.size() + ", first item " + list.get(0));

        // ---------- Wrapper classes: text to numbers ----------
        int year = Integer.parseInt("2024");
        double price = Double.parseDouble("3.75");
        System.out.println("Parsed: " + (year + 1) + " and " + (price * 2));

        try {
            Integer.parseInt("20x4");
        } catch (NumberFormatException e) {
            System.out.println("Integer.parseInt(\"20x4\") threw " + e.getClass().getSimpleName());
        }
    }
}
