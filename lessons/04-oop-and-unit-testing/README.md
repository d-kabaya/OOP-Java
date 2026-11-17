# Lesson 4: OOP in Practice and Unit Testing

**Based on:** Lecture 4 (Motivating OOP and Unit Testing)
**Time needed:** about 1 hour 15 minutes

## What you will learn

By the end of this lesson you should be able to:

- use an `enum` to represent a fixed set of constants
- explain **why** we use object-oriented programming
- build a program from several objects that work together
- debug code methodically with print statements
- write JUnit tests, and choose good test cases
- explain what **cohesion** is, and spot a "God class"

---

## 1. Enums: a fixed set of constants

An `enum` is a special kind of class for a **fixed group of constant values**, like days of the
week or clothing sizes. The constants are written in UPPER CASE.

```java
public enum Day {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
}

Day today = Day.FRIDAY;
if (today == Day.FRIDAY) {
    System.out.println("Weekend soon!");
}
```

Why not use `int`s or `String`s? With `int day = 5`, nothing stops someone writing
`day = 57`. With `Day today`, the **only** possible values are the seven constants, so a
mistake won't compile.

Enums can also have their own fields, constructors and methods:

```java
public enum Size {
    EXTRA_SMALL("XS"), SMALL("S"), MEDIUM("M"), LARGE("L");

    private String abbreviation;

    Size(String abbreviation) {          // enum constructors are always private
        this.abbreviation = abbreviation;
    }

    public String getAbbreviation() {
        return abbreviation;
    }
}

System.out.println(Size.MEDIUM.getAbbreviation());    // M
```

From another class, refer to the constants **through the enum's name**: `Size.SMALL`. You can
also `switch` on an enum, and loop over every value with `Size.values()`.

---

## 2. Why OOP?

**Code reuse.** Write a class once and use it many times, in many programs, without changing it.

**Easier to understand and change.** Each class has one clear job, so it's easier to find
where something happens.

**Encapsulation** means hiding the implementation details inside a class, and publishing an
**interface** to it: its public methods. Remember the laundry object from Lesson 3: you don't
need to know *how* it washes your clothes, only that it does.

This leads to a trade-off you should understand:

| | Can you change it later? |
|---|---|
| **Private code** (method bodies, private fields) | ✅ Yes, freely. Nobody else can see it, so as long as the results stay correct, nothing breaks |
| **The interface** (public method names, parameters, return types, constructors) | ⚠️ Not easily. Other code calls these, so changing them breaks that code |

So **choose your public methods carefully**: they are a promise to everyone who uses your class.

---

## 3. Objects working together: Students and Modules

The lecture builds a `Student` class with a name, a student ID, the modules they take (each with
a mark), an average, and an expected classification. A good design gives each idea its own class:

```java
public class Module {
    private String name;
    private int mark;
    ...
}

public class Student {
    private String name;
    private int id;
    private ArrayList<Module> modules;    // a Student HAS Modules

    public void addModule(Module m) { modules.add(m); }

    public double getAverage() { ... }    // asks each Module for its mark
}
```

`Student` doesn't store marks directly: it asks each `Module` object for its mark. Objects
**send messages** to each other by calling methods.

---

## 4. Debugging with print statements

The lecture's method for "my code is broken":

1. Add print statements at key points: entering and leaving methods and loops.
2. Run it, and see which message was printed **last** before things went wrong.
3. Add **more** print statements around that area, to narrow it down.
4. Repeat until you find the exact line that misbehaves.
5. Compare what *actually* happens with what you *expected* to happen.

```java
System.out.println("DEBUG: entering getAverage, modules = " + modules.size());
```

Build up gradually, otherwise you'll drown in output. And **test as you go**: the more code you
write without testing, the longer debugging takes.

---

## 5. Unit testing with JUnit

A **unit** is the smallest testable piece of a program. In OOP that's usually **one method**.
A **unit test** checks that one method does what it should.

### The problem with testing by printing

```java
Student s = new Student("Simon", 1234556);
s.addModule(new Module("Java", 60));
s.addModule(new Module("Python", 40));
System.out.println("Average is: " + s.getAverage());   // is that right? YOU have to check
```

This works once, but **you** have to read the output and decide whether it's right, every time
you change your code. As the lecture puts it: *"we are lazy, and often arrogant: we think we're
right, until we somehow notice we're not."*

### The JUnit way

You write down the **expected** answer once. JUnit compares it with the actual result
**automatically**, and shows a ✅ or ❌. You can re-run hundreds of tests in a second.

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StudentTest {

    @Test
    public void averageOfTwoModules() {
        // Arrange: set up the objects
        Student s = new Student("Simon", 1234556);
        s.addModule(new Module("Java", 60));
        s.addModule(new Module("Python", 40));

        // Act: call the method being tested
        double result = s.getAverage();

        // Assert: check the result
        assertEquals(50.0, result, 0.001);
    }
}
```

- `@Test` marks a method as a test. Without it, JUnit ignores the method.
- `assertEquals(expected, actual, delta)`: **expected first**. For `double`s, give a **delta**
  ("close enough"), because decimals aren't stored exactly.
- Name each test after what it checks, so a failure tells you what broke.

### The assertions you'll use most

| Assertion | Passes when |
|---|---|
| `assertEquals(expected, actual)` | they're equal (uses `.equals()` for objects) |
| `assertEquals(expected, actual, delta)` | two `double`s are within `delta` |
| `assertTrue(condition)` / `assertFalse(condition)` | the condition is true / false |
| `assertNull(x)` / `assertNotNull(x)` | `x` is / isn't `null` |
| `assertThrows(SomeException.class, () -> code)` | the code throws that exception |

`assertThrows` uses a **lambda** (`() -> ...`), which you'll meet in Lesson 9. For now, read it
as: *"here is some code; don't run it yet; `assertThrows` will run it and watch for the
exception."*

### `@BeforeEach`

If every test needs the same objects, create them in a `@BeforeEach` method. It runs before
**every** test, so each test starts fresh and tests can't affect each other.

```java
private Student s;

@BeforeEach
public void setUp() {
    s = new Student("Simon", 1234556);
}
```

### Choosing good test cases

The goal is that your code *"behaves appropriately with 'weird' inputs"*. For each method, think about:

| Kind of test | Question | Example for a mark from 0 to 100 |
|---|---|---|
| Normal | Does it work for ordinary values? | 65 |
| Boundary | Does it work at the exact limits? | 0 and 100 should be accepted |
| Just outside | Does it reject values just past the limits? | -1 and 101 should be rejected |
| Empty / special | What about nothing at all? | average of **no** modules |

**Bugs hide at boundaries.** A test with 65 won't notice `>` written instead of `>=`, but a test
with exactly 0 will.

**Work out expected answers by hand,** from the requirements. If you copy them from what your code
outputs, a wrong answer becomes a "passing" test.

### Setting up JUnit in IntelliJ

1. Click on your class name, press **Alt + Enter** (Mac: **⌥ + Enter**), and choose **Create Test**.
2. Choose **JUnit 5**. If it says the library is missing, click **Fix**.
3. Write your tests, then click the green ▶ next to the test class.

### Changing requirements

Requirements change all the time. When they do, you change your code, re-run the tests, and any
test that now fails points you straight at what broke. This is one of the biggest benefits of
having tests.

---

## 6. Class diagrams

A **class diagram** (part of UML) shows a program's classes, their instance variables and methods,
and how the classes relate to each other. It's a useful way to plan a design before coding, or to
get an overview of existing code. IntelliJ can generate one for you.

```
┌─────────────────────────┐          ┌─────────────────────┐
│ Student                 │          │ Module              │
├─────────────────────────┤  has 0..*├─────────────────────┤
│ - name: String          │─────────►│ - name: String      │
│ - id: int               │          │ - mark: int         │
│ - modules: ArrayList    │          ├─────────────────────┤
├─────────────────────────┤          │ + getMark(): int    │
│ + addModule(Module)     │          └─────────────────────┘
│ + getAverage(): double  │
└─────────────────────────┘
```
`-` means private and `+` means public.

---

## 7. Cohesion

**Cohesion** measures how closely the parts of a class belong together.

- **High cohesion (good):** everything in the class relates to one clear purpose.
- **Low cohesion (bad):** the class does lots of unrelated things.

A huge class that does everything is called a **"God class"**, and it's a warning sign. The
lecture gives a test: if methods A and B only use variables X and Y, and methods C and D only use
M and N, then the class is really **two classes** squashed together. Split it: A, B, X and Y into
one class, and C, D, M and N into another.

---

## Examples to run

| File | Shows |
|---|---|
| [`Size.java`](examples/Size.java) | An enum with a constructor and a method |
| [`Module.java`](examples/Module.java), [`Student.java`](examples/Student.java) | Two objects working together |
| [`StudentDemo.java`](examples/StudentDemo.java) | Uses the classes above, and loops over an enum |
| [`test/StudentTest.java`](examples/test/StudentTest.java) | JUnit 5 tests for `Student` and `Module` |

Compile and run the demo: `javac Size.java Module.java Student.java StudentDemo.java`, then
`java StudentDemo`. The tests need JUnit 5, so run them in IntelliJ: put all the files in one
project's `src` folder, add JUnit 5 (Alt + Enter on the red import), and run `StudentTest`.

---

## Summary

- An **enum** is a fixed set of named constants. It can have fields, a constructor and methods.
- OOP gives **reuse**, **readability** and **encapsulation**. Private code can change freely, but
  the public interface should stay stable.
- Debug by adding print statements **gradually**, narrowing in on the problem.
- A **unit test** checks one method automatically. Use **Arrange, Act, Assert**, `@Test`, and
  `assertEquals(expected, actual)`.
- Test **normal**, **boundary**, **just-outside** and **empty** cases, with expected values worked out by hand.
- Aim for **high cohesion**. A God class should be split up.

---

## Quiz

**Q1.** Why is an `enum` better than an `int` for representing a T-shirt size?

<details><summary>Answer</summary>

With an `int`, any number is allowed, so a value like 57 compiles even though it means nothing.
With an `enum`, the **only** possible values are the constants you listed, so an invalid size
can't happen. The code is also more readable: `Size.MEDIUM` instead of `2`.
</details>

**Q2.** What is printed?

```java
for (Size s : Size.values()) {
    System.out.print(s.getAbbreviation() + " ");
}
```
(with the `Size` enum from this lesson)

<details><summary>Answer</summary>

`XS S M L ` (with a trailing space). `values()` returns every constant in the order they were declared.
</details>

**Q3.** Why is it easy to change the **body** of a public method later, but hard to change its
**name or parameters**?

<details><summary>Answer</summary>

The body is hidden (encapsulated): other code only cares that the method gives the right result,
not how. The name and parameters are part of the class's public **interface**: other code calls
the method using them, so changing them breaks that code.
</details>

**Q4.** What is a unit test, and what is the "unit" in OOP?

<details><summary>Answer</summary>

A unit test automatically checks that one small piece of a program works as expected. In OOP, the
unit is usually **one method**.
</details>

**Q5.** Give two reasons JUnit tests are better than checking printed output by eye.

<details><summary>Answer</summary>

Any two of: they're **automatic** (no need to read output and judge it); they're **repeatable**
(re-run them all in seconds after every change); they **record the expected answer** so nobody has
to remember it; they **point to exactly what broke** when one fails; they catch bugs introduced by
**later changes** (changing requirements).
</details>

**Q6.** What is wrong with this test?

```java
@Test
public void averageTest() {
    Student s = new Student("Simon", 1);
    s.addModule(new Module("Java", 70));
    s.addModule(new Module("Maths", 75));
    assertEquals(s.getAverage(), 72.5);
}
```

<details><summary>Answer</summary>

Two problems:
1. The arguments are **the wrong way round**: it should be `assertEquals(72.5, s.getAverage(), ...)`,
   with expected first. Otherwise a failure message reports the values backwards.
2. There's **no delta** for comparing `double`s. Use `assertEquals(72.5, s.getAverage(), 0.001)`.
</details>

**Q7.** A method accepts marks from 0 to 100 inclusive. List **four** values you would test, and
say what should happen with each.

<details><summary>Answer</summary>

For example:
- `0`: accepted (lower boundary)
- `100`: accepted (upper boundary)
- `-1`: rejected (just below the boundary)
- `101`: rejected (just above the boundary)

A normal value like `65` is also good to include. Boundary values matter most, because that's
where `<` vs `<=` mistakes show up.
</details>

**Q8.** What does `@BeforeEach` do, and why does it help?

<details><summary>Answer</summary>

It marks a method that JUnit runs **before every test**. You put shared set-up there (for example,
creating a fresh `Student`), so you don't repeat it in every test, and each test starts with new
objects. That keeps tests independent of each other.
</details>

**Q9.** Your `getAverage` test with marks 60 and 40 passes, but the method is wrong for 60 and 45
(it returns 52.0 instead of 52.5). What is the likely bug, and what lesson does this teach about
choosing test cases?

<details><summary>Answer</summary>

**Integer division**: something like `int total / int count`, which throws away the `.5` (105 / 2 = 52).
The lesson: one test isn't enough. 60 and 40 happened to give a whole number, which hid the bug.
Choose test cases that exercise different situations, such as an average that isn't a whole number.
</details>

**Q10.** What is cohesion, and what is a "God class"?

<details><summary>Answer</summary>

**Cohesion** is how closely the parts of a class belong together. High cohesion (everything serves
one purpose) is good. A **God class** is a very large class that does too many unrelated things:
it has low cohesion and should be split into smaller, focused classes.
</details>

---

[← Previous: Lesson 3](../03-classes-and-objects/README.md) · [Back to all lessons](../../README.md) · [Next: Lesson 5, Encapsulation →](../05-encapsulation/README.md)
