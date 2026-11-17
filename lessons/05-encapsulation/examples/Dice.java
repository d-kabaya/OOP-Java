import java.util.Random;

/**
 * Lesson 5: a utility class made only of static methods.
 * You never need to create a Dice object.
 */
public class Dice {

    private static Random rng = new Random();

    public static int roll1D6() {
        int d = rng.nextInt(6);      // 0 to 5
        return 1 + d;                // 1 to 6
    }

    public static int roll2D6() {
        return roll1D6() + roll1D6();
    }
}
