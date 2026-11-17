/**
 * Lesson 9: anonymous inner classes, lambdas, and the arrow switch.
 *
 * Compile: javac *.java
 * Run:     java LambdaDemo
 */
public class LambdaDemo {

    // Takes ANY Calculator: the caller decides the calculation by passing a lambda
    public static void printResult(String label, Calculator calc, double w, double h) {
        System.out.println(label + ": " + calc.calculate(w, h));   // the lambda's code runs here
    }

    public static void main(String[] args) {
        // ---------- 1. Anonymous inner class ----------
        Calculator areaOld = new Calculator() {
            @Override
            public double calculate(double width, double height) {
                return width * height;
            }
        };
        System.out.println("Anonymous inner class: " + areaOld.calculate(4, 5));

        // ---------- 2. The same thing as a lambda: short and long forms ----------
        Calculator area = (width, height) -> width * height;
        Calculator perimeter = (double width, double height) -> {
            return 2 * (width + height);          // braces mean you must write return
        };
        System.out.println("Lambda area:      " + area.calculate(4, 5));
        System.out.println("Lambda perimeter: " + perimeter.calculate(4, 5));

        // ---------- 3. Passing lambdas to a method ----------
        printResult("Area", area, 4, 5);
        printResult("Ratio", (w, h) -> w / h, 4, 5);
        printResult("Bigger side", (w, h) -> Math.max(w, h), 4, 5);

        // ---------- 4. A built-in functional interface: Runnable ----------
        Runnable hello = () -> System.out.println("Hello from a Runnable!");
        hello.run();

        // ---------- 5. Arrow switch: no fall-through, no break ----------
        int itemCode = 2;
        switch (itemCode) {
            case 1, 2 -> System.out.println("It's a laptop!");
            case 3 -> System.out.println("It's a desktop!");
            default -> System.out.println("Unknown device!");
        }

        // ...and it can produce a value
        String day = "SUNDAY";
        String message = switch (day) {
            case "MONDAY" -> "Start of the work week";
            case "SATURDAY", "SUNDAY" -> "Weekend!";
            default -> "Midweek day";
        };
        System.out.println(day + ": " + message);
    }
}
