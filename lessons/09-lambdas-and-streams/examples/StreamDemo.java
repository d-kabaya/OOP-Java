import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Lesson 9: stream pipelines.
 *
 * Compile: javac *.java
 * Run:     java StreamDemo
 */
public class StreamDemo {

    public static void main(String[] args) {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "Dee", "Bob");

        // ---------- The loop version ----------
        List<String> loopResult = new ArrayList<String>();
        for (String n : names) {
            if (n.length() > 3) {
                loopResult.add(n.toUpperCase());
            }
        }
        System.out.println("Loop:   " + loopResult);

        // ---------- The stream version: source -> intermediate -> terminal ----------
        List<String> streamResult = names.stream()
                .filter(n -> n.length() > 3)
                .map(n -> n.toUpperCase())
                .collect(Collectors.toList());
        System.out.println("Stream: " + streamResult);
        System.out.println("Original list is unchanged: " + names);

        // ---------- distinct and sorted ----------
        List<String> unique = names.stream()
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        System.out.println("Distinct, sorted: " + unique);

        // ---------- Terminal operations that return numbers and booleans ----------
        long longNames = names.stream().filter(n -> n.length() > 3).count();
        int totalLetters = names.stream().mapToInt(n -> n.length()).sum();
        boolean anyStartWithC = names.stream().anyMatch(n -> n.startsWith("C"));
        System.out.println("Names longer than 3: " + longNames);
        System.out.println("Total letters: " + totalLetters);
        System.out.println("Any start with C? " + anyStartWithC);

        // ---------- Streams of objects ----------
        List<Person> people = Arrays.asList(
                new Person("Alice", 30), new Person("Bob", 25), new Person("Charlie", 18));
        List<String> adultsByAge = people.stream()
                .filter(p -> p.getAge() >= 21)
                .sorted((a, b) -> Integer.compare(a.getAge(), b.getAge()))
                .map(Person::getName)
                .collect(Collectors.toList());
        System.out.println("Adults (21+), youngest first: " + adultsByAge);

        double averageAge = people.stream().mapToInt(Person::getAge).average().orElse(0);
        System.out.println("Average age: " + averageAge);

        // ---------- Laziness: no terminal operation, so nothing runs ----------
        names.stream().filter(n -> {
            System.out.println("filter ran on " + n);   // never printed
            return true;
        });
        System.out.println("No terminal operation, so the filter above never ran.");
    }
}
