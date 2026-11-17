import java.util.ArrayList;
import java.util.Collections;

/**
 * Lesson 9: three ways to sort.
 *
 * Compile: javac *.java
 * Run:     java SortingDemo
 */
public class SortingDemo {

    public static void main(String[] args) {
        ArrayList<Person> people = new ArrayList<Person>();
        people.add(new Person("Alice", 30));
        people.add(new Person("Bob", 25));
        people.add(new Person("Charlie", 18));
        System.out.println("Original:                  " + people);

        // 1. Comparable: Collections.sort uses Person's compareTo
        Collections.sort(people);
        System.out.println("Collections.sort:          " + people);

        // 2. A lambda: oldest first (b and a swapped)
        people.sort((a, b) -> Integer.compare(b.getAge(), a.getAge()));
        System.out.println("Lambda, oldest first:      " + people);

        // 3. A lambda that sorts by name instead
        people.sort((a, b) -> a.getName().compareTo(b.getName()));
        System.out.println("Lambda, by name:           " + people);

        // 4. A method reference: same as (a, b) -> a.compareTo(b)
        people.sort(Person::compareTo);
        System.out.println("Method reference:          " + people);

        // Method reference with forEach: same as p -> System.out.println(p)
        people.forEach(System.out::println);
    }
}
