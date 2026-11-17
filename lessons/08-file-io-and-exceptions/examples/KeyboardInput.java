import java.util.Scanner;

/**
 * Lesson 8: validating keyboard input with two loops (from the lecture).
 * This one is interactive: type values when asked.
 *
 * Compile: javac KeyboardInput.java
 * Run:     java KeyboardInput
 */
public class KeyboardInput {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter an integer grade from 0 to 100:");
        int grade = -1;                                 // start invalid so the outer loop runs

        while (grade < 0 || grade > 100) {              // outer loop: wrong RANGE
            while (!in.hasNextInt()) {                  // inner loop: wrong TYPE
                String bad = in.next();                 // throw away the bad token, or loop forever
                System.out.println("You entered <" + bad + ">, try again");
            }
            grade = in.nextInt();                       // safe: we know it's an int
            if (grade < 0 || grade > 100) {
                System.out.println("The value must be between 0 and 100, try again");
            }
        }
        System.out.println("Thanks! Your grade is " + grade);
    }
}
