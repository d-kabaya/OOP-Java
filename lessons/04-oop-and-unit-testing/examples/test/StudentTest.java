import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Lesson 4: JUnit 5 tests for Student and Module.
 * Run in IntelliJ with JUnit 5 added to the project.
 */
public class StudentTest {

    private Student s;

    // Runs before EVERY test, so each test gets a brand-new Student
    @BeforeEach
    public void setUp() {
        s = new Student("Simon", 1234556);
    }

    // ---------- Normal cases ----------

    // The lecture's example: (60 + 40) / 2 = 50
    @Test
    public void averageOfTwoModules() {
        s.addModule(new Module("Java", 60));
        s.addModule(new Module("Python", 40));
        assertEquals(50.0, s.getAverage(), 0.001);
    }

    // (60 + 45) / 2 = 52.5: catches integer division
    @Test
    public void averageNotWholeNumber() {
        s.addModule(new Module("Java", 60));
        s.addModule(new Module("Python", 45));
        assertEquals(52.5, s.getAverage(), 0.001);
    }

    // ---------- Empty case ----------

    // No modules: the average is 0, and nothing crashes
    @Test
    public void averageWithNoModules() {
        assertEquals(0.0, s.getAverage(), 0.001);
    }

    // ---------- Boundary cases ----------

    @Test
    public void markOfZeroAccepted() {
        assertEquals(0, new Module("Java", 0).getMark());
    }

    @Test
    public void markOfHundredAccepted() {
        assertEquals(100, new Module("Java", 100).getMark());
    }

    // ---------- Just outside the boundaries ----------

    @Test
    public void markBelowZeroRejected() {
        assertThrows(IllegalArgumentException.class, () -> new Module("Java", -1));
    }

    @Test
    public void markAboveHundredRejected() {
        assertThrows(IllegalArgumentException.class, () -> new Module("Java", 101));
    }

    // ---------- Other behaviour ----------

    @Test
    public void addModuleIncreasesCount() {
        s.addModule(new Module("Java", 60));
        assertEquals(1, s.getNumModules());
    }
}
