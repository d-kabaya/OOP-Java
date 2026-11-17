/**
 * Lesson 9: a Person with a natural order (by age), via Comparable.
 */
public class Person implements Comparable<Person> {

    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    // Negative: this comes first. Positive: other comes first. Zero: equal.
    @Override
    public int compareTo(Person other) {
        return Integer.compare(this.age, other.age);     // youngest first
    }

    @Override
    public String toString() {
        return name + " (" + age + ")";
    }
}
